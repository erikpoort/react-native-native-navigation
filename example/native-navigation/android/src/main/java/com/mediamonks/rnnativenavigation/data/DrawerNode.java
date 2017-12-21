package com.mediamonks.rnnativenavigation.data;

import android.view.Gravity;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.mediamonks.rnnativenavigation.factory.BaseFragment;
import com.mediamonks.rnnativenavigation.factory.DrawerFragment;
import com.mediamonks.rnnativenavigation.factory.NodeHelper;

import java.util.Arrays;

/**
 * Created by erik on 18/09/2017.
 * example 2017
 */

public class DrawerNode extends BaseNode implements Node {
    public static String JS_NAME = "DrawerView";

    private static final String LEFT = "left";
    private static final String CENTER = "center";
    private static final String RIGHT = "right";
    private static final String SIDE = "side";

    private ReactInstanceManager _instanceManager;
    private Node _leftNode;
    private Node _centerNode;
    private Node _rightNode;
    private int _side;

    @Override
    public BaseFragment generateFragment() {
        DrawerFragment fragment = new DrawerFragment();
        fragment.setNode(this);
        return fragment;
    }

    @Override
    public void setInstanceManager(ReactInstanceManager instanceManager) {
        _instanceManager = instanceManager;
    }

    private ReactInstanceManager getInstanceManager() {
        return _instanceManager;
    }

    @Override
    public void setData(ReadableMap map) {
        super.setData(map);

        try {
            if (map.hasKey(LEFT)) {
                _leftNode = NodeHelper.nodeFromMap(map.getMap(LEFT), getInstanceManager());
            }
            if (map.hasKey(CENTER)) {
                _centerNode = NodeHelper.nodeFromMap(map.getMap(CENTER), getInstanceManager());
            }
            if (map.hasKey(RIGHT)) {
                _rightNode = NodeHelper.nodeFromMap(map.getMap(RIGHT), getInstanceManager());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String side = map.getString(SIDE);
        String[] sides = new String[]{LEFT, CENTER, RIGHT};
        Integer[] gravities = new Integer[]{Gravity.START, Gravity.NO_GRAVITY, Gravity.END};
        int index = Arrays.asList(sides).indexOf(side);
        if (index >= 0) {
            _side = gravities[index];
        } else {
            _side = Gravity.NO_GRAVITY;
        }
    }

    @Override
    public WritableMap data() {
        WritableMap map = super.data();

        if (_leftNode != null) {
            map.putMap(LEFT, _leftNode.data());
        }
        if (_centerNode != null) {
            map.putMap(CENTER, _centerNode.data());
        }
        if (_rightNode != null) {
            map.putMap(RIGHT, _rightNode.data());
        }

        String[] sides = new String[]{LEFT, CENTER, RIGHT};
        Integer[] gravities = new Integer[]{Gravity.START, Gravity.NO_GRAVITY, Gravity.END};
        int index = Arrays.asList(gravities).indexOf(_side);
        if (index >= 0) {
            map.putString(SIDE, sides[index]);
        } else {
            map.putString(SIDE, CENTER);
        }
        return map;
    }

    @Override
    public String getTitle() {
        return _centerNode.getTitle();
    }

    public Node getLeftNode() {
        return _leftNode;
    }

    public Node getCenterNode() {
        return _centerNode;
    }

    public Node getRightNode() {
        return _rightNode;
    }

    public int getSide() {
        return _side;
    }

    public void setSide(int side) {
        _side = side;
    }
}
