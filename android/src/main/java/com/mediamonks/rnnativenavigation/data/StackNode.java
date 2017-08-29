package com.mediamonks.rnnativenavigation.data;

import android.util.Log;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableNativeMap;
import com.mediamonks.rnnativenavigation.factory.BaseFragment;
import com.mediamonks.rnnativenavigation.factory.NodeHelper;
import com.mediamonks.rnnativenavigation.factory.StackFragment;

import java.util.ArrayList;

/**
 * Created by erik on 12/08/2017.
 * RNNativeNavigation 2017
 */

public class StackNode implements Node
{
	public static String jsName = "StackView";

	private ArrayList<Node> _stack;
	private ReactInstanceManager _instanceManager;
	private ReadableMap _data;

	@Override
	public BaseFragment<StackNode> getFragment()
	{
		StackFragment fragment = new StackFragment();
		fragment.setNode(this);
		return fragment;
	}

	public ArrayList<Node> getStack()
	{
		return _stack;
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

		ArrayList<Node> stack = new ArrayList<>();
		ReadableArray stackArray = map.getArray("stack");

		int leni = stackArray.size();
		for (int i = 0; i < leni; ++i) {
			ReadableMap obj = stackArray.getMap(i);
			try
			{
				stack.add(NodeHelper.nodeFromMap(obj, getInstanceManager()));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		_stack = stack;
	}

	public ReadableMap getData()
	{
		return _data;
	}
}
