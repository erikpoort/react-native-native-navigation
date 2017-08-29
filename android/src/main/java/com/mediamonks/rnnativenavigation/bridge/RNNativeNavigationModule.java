package com.mediamonks.rnnativenavigation.bridge;

import android.util.Log;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

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

		
	}
}
