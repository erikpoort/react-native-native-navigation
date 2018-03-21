package com.mediamonks.rnnativenavigation.data;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.mediamonks.rnnativenavigation.factory.BaseFragment;
import com.mediamonks.rnnativenavigation.factory.NodeHelper;
import com.mediamonks.rnnativenavigation.factory.StackFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by erik on 12/08/2017.
 * RNNativeNavigation 2017
 */

public class StackNode extends BaseNode {
	public static final String PUSH = "push";
	public static final String POP = "pop";
	public static final String POP_TO = "popTo";
	public static final String POP_TO_ROOT = "popToRoot";

	public static String JS_NAME = "StackView";

	public static Map<String, Object> getConstants() {
		Map<String, Object> map = new HashMap<>();
		map.put(PUSH, PUSH);
		map.put(POP, POP);
		map.put(POP_TO, POP_TO);
		map.put(POP_TO_ROOT, POP_TO_ROOT);
		return map;
	}

	private static final String STACK = "stack";

	private Stack<Node> _stack;

	@Override
	public BaseFragment<StackNode> generateFragment() {
		StackFragment fragment = new StackFragment();
		fragment.setNode(this);
		return fragment;
	}

	@Override
	public void setData(ReadableMap map) {
		super.setData(map);

		Stack<Node> stack = new Stack<>();
		ReadableArray stackArray = map.getArray(STACK);

		int leni = stackArray.size();
		for (int i = 0; i < leni; ++i) {
			ReadableMap obj = stackArray.getMap(i);
			try {
				stack.add(NodeHelper.getInstance().nodeFromMap(obj, getInstanceManager()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		_stack = stack;
	}

	@Override
	public HashMap<String, Object> getData() {
		HashMap<String, Object> map = super.getData();
		ArrayList<HashMap<String, Object>> stack = new ArrayList<>();
		for (Node node : _stack) {
			stack.add(node.getData());
		}
		map.put(STACK, stack);
		return map;
	}

	public Stack<Node> getStack() {
		return _stack;
	}
}
