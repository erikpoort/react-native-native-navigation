package com.mediamonks.rnnativenavigation.bridge;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.util.Log;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.mediamonks.rnnativenavigation.data.Node;
import com.mediamonks.rnnativenavigation.factory.NodeHelper;

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
	public String getName()
	{
		return "ReactNativeNativeNavigation";
	}

	@ReactMethod
	public void onStart(Promise promise)
	{
		ReadableMap state = RNNNState.INSTANCE.state;
		if (state != null) {
			this.setSiteMap(state, promise);
		} else {
			Log.i(kRNNN, "First load");
			String message = "A site map is needed to build the views, call setSiteMap";
			promise.reject(kRNNN, message, new Throwable(message));
		}
	}

	@ReactMethod
	public void setSiteMap(ReadableMap map, Promise promise)
	{
		RNNNState.INSTANCE.state = map;

		try {
			assert getCurrentActivity() != null;
			ReactActivity mainActivity = (ReactActivity) getCurrentActivity();
			ReactApplication mainApplication = (ReactApplication) mainActivity.getApplication();
			ReactNativeHost reactNativeHost = mainApplication.getReactNativeHost();
			ReactInstanceManager reactInstanceManager = reactNativeHost.getReactInstanceManager();
			Node node = NodeHelper.nodeFromMap(map, reactInstanceManager);
			FragmentManager fragmentManager = mainActivity.getFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			Fragment fragment = node.getFragment();
			transaction.replace(android.R.id.content, fragment);
			transaction.commit();
		} catch (Exception e) {
			promise.reject(kRNNN, "Could not start app", e);
		}
	}
}
