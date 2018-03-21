package com.mediamonks.rnnativenavigation.data;

import com.facebook.react.bridge.ReadableMap;
import com.mediamonks.rnnativenavigation.factory.BaseFragment;
import com.mediamonks.rnnativenavigation.factory.NodeHelper;
import com.mediamonks.rnnativenavigation.factory.SplitFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by erik on 15/09/2017.
 * example 2017
 */

public class SplitNode extends BaseNode {
	public static final String REPLACE = "replace";

	public static String JS_NAME = "SplitView";

	public static Map<String, Object> getConstants() {
		Map<String, Object> map = new HashMap<>();
		map.put(REPLACE, REPLACE);
		return map;
	}

	private static final String NODE1 = "node1";
	private static final String NODE2 = "node2";
	private static final String AXIS = "axis";

	private static final String HORIZONTAL = "horizontal";
	private static final String VERTICAL = "vertical";

	private Node _node1;
	private Node _node2;
	private int _axis;

	@Override
	public BaseFragment generateFragment() {
		SplitFragment fragment = new SplitFragment();
		fragment.setNode(this);
		return fragment;
	}

	@Override
	public void setData(ReadableMap map) {
		super.setData(map);

		try {
			_node1 = NodeHelper.getInstance().nodeFromMap(map.getMap(NODE1), getInstanceManager());
			_node2 = NodeHelper.getInstance().nodeFromMap(map.getMap(NODE2), getInstanceManager());
		} catch (Exception e) {
			e.printStackTrace();
		}

		_axis = map.getString(AXIS).equals(VERTICAL) ? 1 : 0;
	}

	@Override
	public HashMap<String, Object> getData() {
		HashMap<String, Object> map = super.getData();
		map.put(NODE1, _node1.getData());
		map.put(NODE2, _node2.getData());
		map.put(AXIS, _axis == 1 ? VERTICAL : HORIZONTAL);
		return map;
	}

	public Node getNode1() {
		return _node1;
	}

	public void setNode1(Node node1) {
		_node1 = node1;
	}

	public Node getNode2() {
		return _node2;
	}

	public void setNode2(Node node2) {
		_node2 = node2;
	}

	public int getAxis() {
		return _axis;
	}
}
