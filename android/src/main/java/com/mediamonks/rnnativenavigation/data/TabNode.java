package com.mediamonks.rnnativenavigation.data;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.mediamonks.rnnativenavigation.factory.BaseFragment;
import com.mediamonks.rnnativenavigation.factory.NodeHelper;
import com.mediamonks.rnnativenavigation.factory.TabFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by erik on 10/08/2017.
 * RNNativeNavigation 2017
 */

public class TabNode extends BaseNode {
	public static final String OPEN_TAB = "openTab";

	public static String JS_NAME = "TabView";

	public static Map<String, Object> getConstants() {
		Map<String, Object> map = new HashMap<>();
		map.put(OPEN_TAB, OPEN_TAB);
		return map;
	}

	private static final String TABS = "tabs";
	private static final String SELECTED_TAB = "selectedTab";

	private List<Node> _tabs;
	private int _selectedTab;

	@Override
	public BaseFragment generateFragment() {
		TabFragment fragment = new TabFragment();
		fragment.setNode(this);
		return fragment;
	}

	@Override
	public void setData(ReadableMap map) {
		super.setData(map);

		List<Node> stack = new ArrayList<>();
		ReadableArray stackArray = map.getArray(TABS);

		int leni = stackArray.size();
		for (int i = 0; i < leni; ++i) {
			ReadableMap obj = stackArray.getMap(i);
			try {
				stack.add(NodeHelper.getInstance().nodeFromMap(obj, getInstanceManager()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		_tabs = stack;
		if (map.hasKey(SELECTED_TAB)) {
			_selectedTab = map.getInt(SELECTED_TAB);
		}
	}

	@Override
	public HashMap<String, Object> getData() {
		HashMap<String, Object> map = super.getData();
		ArrayList<HashMap<String, Object>> tabs = new ArrayList<>();
		for (Node node : _tabs) {
			tabs.add(node.getData());
		}
		map.put(TABS, tabs);
		map.put(SELECTED_TAB, _selectedTab);
		return map;
	}

	public List<Node> getTabs() {
		return _tabs;
	}

	public int getSelectedTab() {
		return _selectedTab;
	}

	public void setSelectedTab(int selectedTab) {
		_selectedTab = selectedTab;
	}
}
