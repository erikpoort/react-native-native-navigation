package com.mediamonks.rnnativenavigation.data;

import android.view.Gravity;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.mediamonks.rnnativenavigation.factory.BaseFragment;
import com.mediamonks.rnnativenavigation.factory.DrawerFragment;
import com.mediamonks.rnnativenavigation.factory.NodeHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by erik on 18/09/2017.
 * example 2017
 */

public class DrawerNode extends BaseNode  {
    public static final String OPEN_VIEW = "openView";

    public static String JS_NAME = "DrawerView";
    public static Map<String, Object> getConstants() {
        Map<String, Object> map = new HashMap<>();
        map.put(OPEN_VIEW, OPEN_VIEW);
        return map;
    }

    public static final String LEFT = "left";
    public static final String CENTER = "center";
    public static final String RIGHT = "right";
    private static final String SIDE = "side";

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
    public void setData(ReadableMap map) {
        super.setData(map);

        try {
            if (map.hasKey(LEFT)) {
                _leftNode = NodeHelper.getInstance().nodeFromMap(map.getMap(LEFT), getInstanceManager());
            }
            if (map.hasKey(CENTER)) {
                _centerNode = NodeHelper.getInstance().nodeFromMap(map.getMap(CENTER), getInstanceManager());
            }
            if (map.hasKey(RIGHT)) {
                _rightNode = NodeHelper.getInstance().nodeFromMap(map.getMap(RIGHT), getInstanceManager());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String side = CENTER;
        if (map.hasKey(SIDE)) {
            side = map.getString(SIDE);
        }

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
    public WritableMap getData() {
        WritableMap map = super.getData();

        if (_leftNode != null) {
            map.putMap(LEFT, _leftNode.getData());
        }
        if (_centerNode != null) {
            map.putMap(CENTER, _centerNode.getData());
        }
        if (_rightNode != null) {
            map.putMap(RIGHT, _rightNode.getData());
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

    public void setLeftNode(Node leftNode) {
        _leftNode = leftNode;
    }

    public Node getCenterNode() {
        return _centerNode;
    }

    public void setCenterNode(Node centerNode) {
        _centerNode = centerNode;
    }

    public Node getRightNode() {
        return _rightNode;
    }

    public void setRightNode(Node rightNode) {
        _rightNode = rightNode;
    }

    public int getSide() {
        return _side;
    }

    public void setSide(int side) {
        _side = side;
    }
}
