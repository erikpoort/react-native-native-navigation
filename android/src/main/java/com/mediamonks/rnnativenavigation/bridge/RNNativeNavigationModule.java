package com.mediamonks.rnnativenavigation.bridge;

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
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.mediamonks.rnnativenavigation.data.Node;
import com.mediamonks.rnnativenavigation.factory.BaseFragment;
import com.mediamonks.rnnativenavigation.factory.NodeHelper;
import com.mediamonks.rnnativenavigation.factory.SingleFragment;
import com.mediamonks.rnnativenavigation.factory.StackFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by erik on 29/08/2017.
 * example 2017
 */

class RNNativeNavigationModule extends ReactContextBaseJavaModule
{
	private static final String kRNNN = "RNNN";

	RNNativeNavigationModule(ReactApplicationContext reactContext)
	{
		super(reactContext);
	}

	@Override
	public void onCatalystInstanceDestroy()
	{
		/*
		 * Clear all existing fragments before Facebook reloads them. The onStart method will
		 * rebuild the fragments.
		 */
		assert getCurrentActivity() != null;
		ReactFragmentActivity mainActivity = (ReactFragmentActivity) getCurrentActivity();
		List<Fragment> fragments = mainActivity.getSupportFragmentManager().getFragments();
		for (Fragment fragment : fragments)
		{
			if (fragment != null)
			{
				FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
				transaction.remove(fragment);
				transaction.commit();
			}
		}

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
		HashMap state = RNNNState.INSTANCE.state;
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
	public void setSiteMap(ReadableMap map, Promise promise)
	{
		RNNNState.INSTANCE.state = map.toHashMap();

		try
		{
			assert getCurrentActivity() != null;
			ReactFragmentActivity mainActivity = (ReactFragmentActivity) getCurrentActivity();
			Node node = NodeHelper.nodeFromMap(map, getReactInstanceManager());
			FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			Fragment fragment = node.getFragment();
			transaction.replace(android.R.id.content, fragment);
			transaction.commit();
			promise.resolve(true);
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
		List<Fragment> fragments = mainActivity.getSupportFragmentManager().getFragments();
		int leni = fragments.size();
		for (int i = leni - 1; i >= 0; --i)
		{
			final Fragment fragment = fragments.get(i);
			if (fragment instanceof StackFragment)
			{
				mainActivity.runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						StackFragment baseFragment = (StackFragment) fragment;
						callback.invoke(baseFragment.onBackPressed());
					}
				});
				return;
			}
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
			List<Fragment> fragments = mainActivity.getSupportFragmentManager().getFragments();
			BaseFragment rootFragment = (BaseFragment) fragments.get(0);
			int lastSlash = node.getScreenID().lastIndexOf("/");
			final BaseFragment findFragment = rootFragment.fragmentForPath(node.getScreenID().substring(0, lastSlash));

			HashMap newState = RNNNState.INSTANCE.state;
			ArrayList stack = (ArrayList) newState.get("stack");
			stack.add(screen.toHashMap());
			newState.put("stack", stack);
			RNNNState.INSTANCE.state = newState;

			callback.invoke(Arguments.makeNativeMap(newState));

			mainActivity.runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					if (findFragment != null)
					{
						SingleFragment singleFragment = (SingleFragment) findFragment;
						singleFragment.getStackFragment().push(node);
					}
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
}
