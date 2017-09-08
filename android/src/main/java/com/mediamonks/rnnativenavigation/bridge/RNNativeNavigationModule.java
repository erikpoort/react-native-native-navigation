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
import com.facebook.react.bridge.WritableMap;
import com.mediamonks.rnnativenavigation.data.Node;
import com.mediamonks.rnnativenavigation.data.StackNode;
import com.mediamonks.rnnativenavigation.factory.BaseFragment;
import com.mediamonks.rnnativenavigation.factory.NodeHelper;
import com.mediamonks.rnnativenavigation.factory.SingleFragment;
import com.mediamonks.rnnativenavigation.factory.StackFragment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Created by erik on 29/08/2017.
 * example 2017
 */

class RNNativeNavigationModule extends ReactContextBaseJavaModule implements LifecycleEventListener
{
	private static final String kRNNN = "RNNN";

	private Set<BaseFragment> _fragments;
	private final FragmentManager.FragmentLifecycleCallbacks _lifecycleCallbacks;

	RNNativeNavigationModule(ReactApplicationContext reactContext)
	{
		super(reactContext);

		Log.i("MMM i", this.toString());
		_fragments = new HashSet<>();
		_lifecycleCallbacks = new FragmentManager.FragmentLifecycleCallbacks() {
			@Override
			public void onFragmentAttached(FragmentManager fm, Fragment f, Context context)
			{
				super.onFragmentAttached(fm, f, context);

				if (f instanceof BaseFragment)
				{
					BaseFragment baseFragment = (BaseFragment) f;
					_fragments.add(baseFragment);
				}
			}

			@Override
			public void onFragmentDetached(FragmentManager fm, Fragment f)
			{
				super.onFragmentDetached(fm, f);
				BaseFragment baseFragment = (BaseFragment) f;
				_fragments.remove(baseFragment);
			}
		};

		reactContext.addLifecycleEventListener(this);
	}

	@Override
	public void onCatalystInstanceDestroy()
	{
		/*
		 * Save the state of the application before reload
		 */
		BaseFragment anyFragment = (BaseFragment) _fragments.toArray()[0];
		SingleFragment currentFragment = anyFragment.getCurrentFragment();
		BaseFragment rootFragment = currentFragment.getRootFragment();
		WritableMap currentState = rootFragment.getNode().data();
		RNNNState.INSTANCE.state = currentState.toHashMap();

		/*
		 * Clear all existing fragments before Facebook reloads them. The onStart method will
		 * rebuild the fragments.
		 */
		for (Fragment fragment : _fragments)
		{
			if (fragment != null)
			{
				FragmentTransaction transaction = fragment.getActivity().getSupportFragmentManager().beginTransaction();
				transaction.remove(fragment);
				transaction.commit();
			}
		}
		Log.i("MMM d", this.toString());

		super.onCatalystInstanceDestroy();
	}

	@Override
	public String getName()
	{
		return "ReactNativeNativeNavigation";
	}

	@ReactMethod
	public void onStart(Callback callback)
	{
		HashMap <String, Object> state = RNNNState.INSTANCE.state;
		if (state == null)
		{
			Log.i(kRNNN, "First load");
			callback.invoke();
		}
		else
		{
			Log.i(kRNNN, "Reload");
			callback.invoke(Arguments.makeNativeMap(state));
		}
	}

	@ReactMethod
	public void setSiteMap(ReadableMap map, final Promise promise)
	{
		RNNNState.INSTANCE.state = map.toHashMap();

		try
		{
			assert getCurrentActivity() != null;
			ReactFragmentActivity mainActivity = (ReactFragmentActivity) getCurrentActivity();
			final Node node = NodeHelper.nodeFromMap(map, getReactInstanceManager());
			final FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();

			mainActivity.runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					FragmentTransaction transaction = fragmentManager.beginTransaction();
					Fragment fragment = node.getFragment();
					transaction.replace(android.R.id.content, fragment);
					transaction.commit();
					promise.resolve(true);
				}
			});
		}
		catch (Exception e)
		{
			promise.reject(kRNNN, "Could not start app", e);
		}
	}

	@ReactMethod
	public void handleBackButton(final Callback callback)
	{
		assert getCurrentActivity() != null;
		ReactFragmentActivity mainActivity = (ReactFragmentActivity) getCurrentActivity();
		BaseFragment anyFragment = (BaseFragment) _fragments.toArray()[0];
		final BaseFragment rootFragment = getRootFragment(anyFragment.getNode());
		BaseFragment currentFragment = rootFragment.getCurrentFragment();
		final StackFragment stackFragment = currentFragment.getStackFragment();

		if (stackFragment != null) {
			mainActivity.runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					callback.invoke(stackFragment.onBackPressed());
				}
			});
			return;
		}

		callback.invoke(false);
	}

	@ReactMethod
	public void push(final ReadableMap screen, Callback callback)
	{
		try
		{
			final Node node = NodeHelper.nodeFromMap(screen, getReactInstanceManager());

			assert getCurrentActivity() != null;
			ReactFragmentActivity mainActivity = (ReactFragmentActivity) getCurrentActivity();
			BaseFragment rootFragment = getRootFragment(node);
			assert rootFragment != null;

			int lastSlash = node.getScreenID().lastIndexOf("/");
			final BaseFragment findFragment = rootFragment.fragmentForPath(node.getScreenID().substring(0, lastSlash));
			if (findFragment == null)
			{
				return;
			}

			StackFragment stackFragment = findFragment.getStackFragment();
			StackNode stackNode = stackFragment.getNode();
			Stack<Node> stack = stackNode.getStack();
			stack.add(node);

			callback.invoke(Arguments.makeNativeMap(rootFragment.getNode().data().toHashMap()));

			mainActivity.runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					SingleFragment singleFragment = (SingleFragment) findFragment;
					singleFragment.getStackFragment().push(node);
				}
			});
		}
		catch (Exception ignored)
		{
		}
	}

	private ReactInstanceManager getReactInstanceManager()
	{
		assert getCurrentActivity() != null;
		ReactFragmentActivity mainActivity = (ReactFragmentActivity) getCurrentActivity();
		ReactApplication mainApplication = (ReactApplication) mainActivity.getApplication();
		ReactNativeHost reactNativeHost = mainApplication.getReactNativeHost();
		return reactNativeHost.getReactInstanceManager();
	}

	private BaseFragment getRootFragment(Node node)
	{
		BaseFragment rootFragment = null;
		String rootPath = node.getScreenID().substring(0, node.getScreenID().indexOf("/", 1));
		for (Fragment findFragment : _fragments)
		{
			if (findFragment != null)
			{
				BaseFragment findBaseFragment = (BaseFragment) findFragment;
				if (findBaseFragment.getNode().getScreenID().equals(rootPath))
				{
					rootFragment = findBaseFragment;
					break;
				}
			}
		}
		return rootFragment;
	}

	@Override
	public void onHostResume()
	{
		ReactFragmentActivity mainActivity = (ReactFragmentActivity) getCurrentActivity();
		assert mainActivity != null;
		mainActivity.getSupportFragmentManager().registerFragmentLifecycleCallbacks(_lifecycleCallbacks, true);
	}

	@Override
	public void onHostPause()
	{
		ReactFragmentActivity mainActivity = (ReactFragmentActivity) getCurrentActivity();
		assert mainActivity != null;
		mainActivity.getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(_lifecycleCallbacks);
	}

	@Override
	public void onHostDestroy()
	{
		_fragments = null;
	}
}
