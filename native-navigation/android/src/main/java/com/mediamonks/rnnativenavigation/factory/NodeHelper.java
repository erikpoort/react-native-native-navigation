package com.mediamonks.rnnativenavigation.factory;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReadableMap;
import com.mediamonks.rnnativenavigation.data.DrawerNode;
import com.mediamonks.rnnativenavigation.data.Node;
import com.mediamonks.rnnativenavigation.data.SingleNode;
import com.mediamonks.rnnativenavigation.data.SplitNode;
import com.mediamonks.rnnativenavigation.data.StackNode;
import com.mediamonks.rnnativenavigation.data.TabNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by erik on 29/08/2017.
 * example 2017
 */

public class NodeHelper {
    private static final String TYPE = "type";

    private static NodeHelper _instance;

    private Map<String, Class<? extends Node>> _nodes;

    private NodeHelper() {
        _nodes = new HashMap<>();
        _nodes.put(SingleNode.JS_NAME, SingleNode.class);
        _nodes.put(StackNode.JS_NAME, StackNode.class);
        _nodes.put(TabNode.JS_NAME, TabNode.class);
        _nodes.put(SplitNode.JS_NAME, SplitNode.class);
        _nodes.put(DrawerNode.JS_NAME, DrawerNode.class);
    }

    public static NodeHelper getInstance() {
        if (_instance == null) {
            _instance = new NodeHelper();
        }
        return _instance;
    }

    public Node nodeFromMap(ReadableMap map, ReactInstanceManager instanceManager) throws Exception {
        if (map == null) {
            return null;
        }

        String key = map.getString(TYPE);
        if (_nodes.containsKey(key)) {
            Class<? extends Node> nodeClass = _nodes.get(key);
            Node nodeObject = nodeClass.newInstance();
            nodeObject.setInstanceManager(instanceManager);
            nodeObject.setData(map);
            return nodeObject;
        }

        return null;
    }

    public void addExternalNodes(Map<String, Class<? extends Node>> externalNodes) {
        _nodes.putAll(externalNodes);
    }
}
