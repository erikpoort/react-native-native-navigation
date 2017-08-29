package com.mediamonks.rnnativenavigation.factory;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReadableMap;
import com.mediamonks.rnnativenavigation.data.Node;
import com.mediamonks.rnnativenavigation.data.SingleNode;
import com.mediamonks.rnnativenavigation.data.StackNode;

import java.util.Arrays;
import java.util.List;

/**
 * Created by erik on 29/08/2017.
 * example 2017
 */

public class NodeHelper
{
	public static Node nodeFromMap(ReadableMap map, ReactInstanceManager instanceManager) throws Exception
	{
		List<String> names = Arrays.asList(SingleNode.jsName, StackNode.jsName);
		List<Class<? extends Node>> classes = Arrays.asList(SingleNode.class, StackNode.class);
		int index = names.indexOf(map.getString("name"));
		if (index >= 0)
		{
			Class<? extends Node> nodeClass = classes.get(index);
			Node nodeObject = nodeClass.newInstance();
			nodeObject.setInstanceManager(instanceManager);
			nodeObject.setData(map);
			return nodeObject;
		}
		return new SingleNode();
	}
}
