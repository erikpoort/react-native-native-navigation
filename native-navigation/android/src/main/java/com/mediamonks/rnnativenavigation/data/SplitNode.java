package com.mediamonks.rnnativenavigation.data;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.mediamonks.rnnativenavigation.factory.BaseFragment;
import com.mediamonks.rnnativenavigation.factory.NodeHelper;
import com.mediamonks.rnnativenavigation.factory.SplitFragment;

/**
 * Created by erik on 15/09/2017.
 * example 2017
 */

public class SplitNode extends BaseNode implements Node {
    public static String JS_NAME = "SplitView";

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
    public WritableMap getData() {
        WritableMap map = super.getData();
        map.putMap(NODE1, _node1.getData());
        map.putMap(NODE2, _node2.getData());
        map.putString(AXIS, _axis == 1 ? VERTICAL : HORIZONTAL);
        return map;
    }

    @Override
    public String getTitle() {
        return _node1.getTitle();
    }

    public Node getNode1() {
        return _node1;
    }

    public Node getNode2() {
        return _node2;
    }

    public int getAxis() {
        return _axis;
    }
}
