package com.mediamonks.rnnativenavigation.data;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReadableMap;
import com.mediamonks.rnnativenavigation.factory.BaseFragment;

import java.util.HashMap;

/**
 * Created by erik on 09/08/2017.
 * RNNativeNavigation 2017
 */

public interface Node {
	void setInstanceManager(ReactInstanceManager instanceManager);

	void setData(ReadableMap map);

	HashMap<String, Object> getData();

	void setShown(boolean shown);

	boolean isShown();

	String getTitle();

	String getScreenID();

	BaseFragment generateFragment();

	String getRootPath();
}
