package com.mediamonks.rnnativenavigation.data;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.mediamonks.rnnativenavigation.factory.BaseFragment;
import com.mediamonks.rnnativenavigation.factory.NodeHelper;
import com.mediamonks.rnnativenavigation.factory.StackFragment;

import java.util.Stack;

/**
 * Created by erik on 12/08/2017.
 * RNNativeNavigation 2017
 */

public class StackNode extends BaseNode implements Node
{
	public static String JS_NAME = "StackView";

	private static final String STACK = "stack";

	private ReactInstanceManager _instanceManager;
	private Stack<Node> _stack;

	@Override
	public BaseFragment<StackNode> getFragment()
	{
		StackFragment fragment = new StackFragment();
		fragment.setNode(this);
		return fragment;
	}

	@Override
	public void setInstanceManager(ReactInstanceManager instanceManager)
	{
		_instanceManager = instanceManager;
	}

	private ReactInstanceManager getInstanceManager()
	{
		return _instanceManager;
	}

	@Override
	public void setData(ReadableMap map)
	{
		super.setData(map);

		Stack<Node> stack = new Stack<>();
		ReadableArray stackArray = map.getArray(STACK);

		int leni = stackArray.size();
		for (int i = 0; i < leni; ++i)
		{
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

	@Override
	public WritableMap data()
	{
		WritableMap map = super.data();
		WritableArray stack = new WritableNativeArray();
		for (Node node : _stack)
		{
			stack.pushMap(node.data());
		}
		map.putArray(STACK, stack);
		return map;
	}

	@Override
	public String getTitle()
	{
		return _stack.peek().getTitle();
	}

	public Stack<Node> getStack()
	{
		return _stack;
	}
}
