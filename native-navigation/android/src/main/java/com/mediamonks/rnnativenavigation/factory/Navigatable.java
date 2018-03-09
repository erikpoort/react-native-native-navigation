package com.mediamonks.rnnativenavigation.factory;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;

/**
 * Created by erik on 09/03/2018.
 * example 2018
 */

public interface Navigatable {
	void callMethodWithName(String name, ReadableMap arguments, RNNNFragment rootFragment, Callback callback);
}
