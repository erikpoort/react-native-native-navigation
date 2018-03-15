package com.mediamonks.rnnativenavigation.bridge;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactFragmentActivity;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.devsupport.interfaces.DevOptionHandler;
import com.mediamonks.rnnativenavigation.data.Node;
import com.mediamonks.rnnativenavigation.factory.BaseFragment;
import com.mediamonks.rnnativenavigation.factory.Navigatable;
import com.mediamonks.rnnativenavigation.factory.NodeHelper;
import com.mediamonks.rnnativenavigation.factory.RNNNFragment;
import com.mediamonks.rnnativenavigation.factory.SingleFragment;
import com.mediamonks.rnnativenavigation.factory.StackFragment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

/**
 * Created by erik on 29/08/2017.
 * example 2017
 */

public class RNNativeNavigationModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
	private static final String kRNNN = "RNNN";

	private Set<RNNNFragment> _fragments;
	private final FragmentManager.FragmentLifecycleCallbacks _lifecycleCallbacks;

	public RNNativeNavigationModule(ReactApplicationContext reactContext, Map<String, Class<? extends Node>> externalNodes) {
		super(reactContext);

		_fragments = new HashSet<>();
		_lifecycleCallbacks = new FragmentManager.FragmentLifecycleCallbacks() {
			@Override
			public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
				super.onFragmentAttached(fm, f, context);

				if (f instanceof RNNNFragment) {
					RNNNFragment baseFragment = (RNNNFragment) f;
					_fragments.add(baseFragment);
				}
			}

			@Override
			public void onFragmentDetached(FragmentManager fm, Fragment f) {
				super.onFragmentDetached(fm, f);
				if (f instanceof RNNNFragment) {
					RNNNFragment baseFragment = (RNNNFragment) f;
					_fragments.remove(baseFragment);
				}
			}
		};

		NodeHelper.getInstance().addExternalNodes(externalNodes);

		reactContext.addLifecycleEventListener(this);
	}

	@Override
	public void onCatalystInstanceDestroy() {
		/*
         * Save the state of the application before reload
		 */
		if (_fragments.size() > 0) {
			RNNNFragment anyFragment = (RNNNFragment) _fragments.toArray()[0];
			final RNNNFragment rootFragment = getRootFragment(anyFragment.getNode());
			if (rootFragment != null) {
				RNNNState.INSTANCE.state = rootFragment.getNode().getData();

				assert getCurrentActivity() != null;
				getCurrentActivity().runOnUiThread(new Runnable() {
					@Override public void run() {
						rootFragment.invalidate();
					}
				});
			}
		}

		super.onCatalystInstanceDestroy();
	}

	@Override
	public String getName() {
		return "ReactNativeNativeNavigation";
	}

	@Nullable @Override public Map<String, Object> getConstants() {
		return NodeHelper.getInstance().getConstants();
	}

	@ReactMethod
	public void onStart(Callback callback) {
		HashMap<String, Object> state = RNNNState.INSTANCE.state;
		if (state == null) {
			Log.i(kRNNN, "First load");
			callback.invoke();
		} else {
			Log.i(kRNNN, "Reload");
			callback.invoke(Arguments.makeNativeMap(state));
		}
	}

	@ReactMethod
	public void setSiteMap(ReadableMap map, final Promise promise) {
		RNNNState.INSTANCE.state = map.toHashMap();

		try {
			assert getCurrentActivity() != null;
			ReactFragmentActivity mainActivity = (ReactFragmentActivity) getCurrentActivity();
			final Node node = NodeHelper.getInstance().nodeFromMap(map, getReactInstanceManager());
			final FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();

			mainActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					FragmentTransaction transaction = fragmentManager.beginTransaction();
					Fragment fragment = node.generateFragment();
					transaction.replace(android.R.id.content, fragment);
					transaction.commitNowAllowingStateLoss();
					promise.resolve(true);
				}
			});
		} catch (Exception e) {
			promise.reject(kRNNN, "Could not start app", e);
		}
	}

	@ReactMethod
	public void callMethodOnNode(String navigator, String methodName, ReadableMap arguments, Callback callback) {
		RNNNFragment anyFragment = (RNNNFragment) _fragments.toArray()[0];
		final BaseFragment rootFragment = (BaseFragment) getRootFragment(anyFragment.getNode());

		BaseFragment findFragment = rootFragment.fragmentForPath(navigator);
		if (findFragment == null) {
			return;
		}

		if (findFragment instanceof Navigatable) {
			((Navigatable) findFragment).callMethodWithName(methodName, arguments, rootFragment, callback);
		}
	}

	@ReactMethod
	public void handleBackButton(final Callback callback) {
		assert getCurrentActivity() != null;
		ReactFragmentActivity mainActivity = (ReactFragmentActivity) getCurrentActivity();
		RNNNFragment anyFragment = (RNNNFragment) _fragments.toArray()[0];
		final BaseFragment rootFragment = (BaseFragment) getRootFragment(anyFragment.getNode());
		SingleFragment currentFragment = rootFragment.getCurrentFragment();
		final StackFragment stackFragment = currentFragment.getStackFragment();

		if (stackFragment != null) {
			mainActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					callback.invoke(stackFragment.onBackPressed());
				}
			});
			return;
		}

		callback.invoke(false);
	}

	private ReactInstanceManager getReactInstanceManager() {
		assert getCurrentActivity() != null;
		ReactFragmentActivity mainActivity = (ReactFragmentActivity) getCurrentActivity();
		ReactApplication mainApplication = (ReactApplication) mainActivity.getApplication();
		ReactNativeHost reactNativeHost = mainApplication.getReactNativeHost();
		return reactNativeHost.getReactInstanceManager();
	}

	private RNNNFragment getRootFragment(Node node) {
		RNNNFragment rootFragment = null;
		String rootPath = node.getRootPath();
		for (RNNNFragment findFragment : _fragments) {
			if (findFragment != null) {
				if (findFragment.getNode().getScreenID().equals(rootPath)) {
					rootFragment = findFragment;
					break;
				}
			}
		}
		return rootFragment;
	}

	public void resetNavigation() {
		if (_fragments.size() > 0) {
			RNNNFragment anyFragment = (RNNNFragment) _fragments.toArray()[0];
			final RNNNFragment rootFragment = getRootFragment(anyFragment.getNode());

			assert getCurrentActivity() != null;
			getCurrentActivity().runOnUiThread(new Runnable() {
				@Override public void run() {
					rootFragment.invalidate();


					_fragments = new HashSet<>();
					RNNNState.INSTANCE.state = null;
					getReactInstanceManager().getDevSupportManager().handleReloadJS();
				}
			});
		}
	}

	@Override
	public void onHostResume() {
		ReactFragmentActivity mainActivity = (ReactFragmentActivity) getCurrentActivity();
		assert mainActivity != null;
		mainActivity.getSupportFragmentManager().registerFragmentLifecycleCallbacks(_lifecycleCallbacks, true);

		getReactInstanceManager().getDevSupportManager().addCustomDevOption("Reset navigation", new DevOptionHandler() {
			@Override public void onOptionSelected() {
				resetNavigation();
			}
		});
	}

	@Override
	public void onHostPause() {
		ReactFragmentActivity mainActivity = (ReactFragmentActivity) getCurrentActivity();
		assert mainActivity != null;
		mainActivity.getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(_lifecycleCallbacks);
	}

	@Override
	public void onHostDestroy() {
		_fragments.clear();
	}
}
