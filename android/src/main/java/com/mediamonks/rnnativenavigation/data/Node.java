package com.mediamonks.rnnativenavigation.data;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReadableMap;
import com.mediamonks.rnnativenavigation.factory.BaseFragment;

import java.io.Serializable;

/**
 * Created by erik on 09/08/2017.
 * RNNativeNavigation 2017
 */

public interface Node extends Serializable
{
	BaseFragment getFragment();

	void setInstanceManager(ReactInstanceManager instanceManager);
	void setData(ReadableMap map);

	String getTitle();
}
