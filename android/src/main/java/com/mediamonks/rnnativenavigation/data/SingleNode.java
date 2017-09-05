package com.mediamonks.rnnativenavigation.data;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.mediamonks.rnnativenavigation.factory.BaseFragment;
import com.mediamonks.rnnativenavigation.factory.SingleFragment;

/**
 * Created by erik on 09/08/2017.
 * RNNativeNavigation 2017
 */

public class SingleNode extends BaseNode implements Node
{
	public static String JS_NAME = "SingleView";

	private static final String NAME = "name";

	private ReactInstanceManager _instanceManager;
	private String _title;

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
		super.setData(map);
		_title = map.getString(NAME);
	}

	@Override
	public WritableMap data()
	{
		WritableMap data = super.data();
		data.putString(NAME, _title);
		return data;
	}

	@Override
	public String getTitle()
	{
		return _title;
	}

}
