package com.mediamonks.rnnativenavigation.factory;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReadableMap;
import com.mediamonks.rnnativenavigation.data.BaseNode;
import com.mediamonks.rnnativenavigation.data.Node;
import com.mediamonks.rnnativenavigation.data.SingleNode;
import com.mediamonks.rnnativenavigation.data.StackNode;
import com.mediamonks.rnnativenavigation.data.TabNode;

import java.util.Arrays;
import java.util.List;

/**
 * Created by erik on 29/08/2017.
 * example 2017
 */

public class NodeHelper
{
	private static final String TYPE = "type";

	public static Node nodeFromMap(ReadableMap map, ReactInstanceManager instanceManager) throws Exception
	{
		List<String> names = Arrays.asList(SingleNode.JS_NAME, StackNode.JS_NAME, TabNode.JS_NAME);
		List<? extends Class<? extends BaseNode>> classes = Arrays.asList(SingleNode.class, StackNode.class, TabNode.class);
		int index = names.indexOf(map.getString(TYPE));
		if (index >= 0)
		{
			Class<? extends BaseNode> nodeClass = classes.get(index);
			Node nodeObject = (Node) nodeClass.newInstance();
			nodeObject.setInstanceManager(instanceManager);
			nodeObject.setData(map);
			return nodeObject;
		}
		return new SingleNode();
	}
}
