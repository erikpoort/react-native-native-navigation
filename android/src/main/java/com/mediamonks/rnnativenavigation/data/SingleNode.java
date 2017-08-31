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
	public static String JS_NAME = "SingleView";

	private static final String NAME = "name";
	private static final String SCREEN_ID = "screenID";

	private ReactInstanceManager _instanceManager;
	private String _title;
	private String _screenID;

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
		_title = map.getString(NAME);
		_screenID = map.getString(SCREEN_ID);
	}

	@Override
	public String getTitle()
	{
		return _title;
	}

	public String getScreenID()
	{
		return _screenID;
	}
}
