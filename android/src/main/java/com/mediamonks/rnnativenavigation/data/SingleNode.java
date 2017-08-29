package com.mediamonks.rnnativenavigation.data;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReadableMap;
import com.mediamonks.rnnativenavigation.factory.BaseFragment;
import com.mediamonks.rnnativenavigation.factory.SingleFragment;

/**
 * Created by erik on 09/08/2017.
 * RNNativeNavigation 2017
 */

public class SingleNode implements Node
{
	public static String jsName = "SingleView";

	private ReactInstanceManager _instanceManager;
	private ReadableMap _data;

	@Override
	public BaseFragment<SingleNode> getFragment()
	{
		SingleFragment fragment = new SingleFragment();
		fragment.setNode(this);
		return fragment;
	}

	@Override
	public void setInstanceManager(ReactInstanceManager instanceManager)
	{
		_instanceManager = instanceManager;
	}

	public ReactInstanceManager getInstanceManager()
	{
		return _instanceManager;
	}

	@Override
	public void setData(ReadableMap map)
	{
		_data = map;
	}

	public ReadableMap getData()
	{
		return _data;
	}
}
