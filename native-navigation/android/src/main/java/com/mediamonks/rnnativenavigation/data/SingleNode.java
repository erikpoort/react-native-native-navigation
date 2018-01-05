package com.mediamonks.rnnativenavigation.data;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.mediamonks.rnnativenavigation.factory.BaseFragment;
import com.mediamonks.rnnativenavigation.factory.NodeHelper;
import com.mediamonks.rnnativenavigation.factory.SingleFragment;

/**
 * Created by erik on 09/08/2017.
 * RNNativeNavigation 2017
 */

public class SingleNode extends BaseNode implements Node {
    public static String JS_NAME = "SingleView";

    private static final String NAME = "name";
    private static final String MODAL = "modal";

    private String _title;
    private Node _modal;

    @Override
    public BaseFragment<SingleNode> generateFragment() {
        SingleFragment fragment = new SingleFragment();
        fragment.setNode(this);
        return fragment;
    }

    @Override
    public void setData(ReadableMap map) {
        super.setData(map);
        _title = map.getString(NAME);

        if (map.hasKey(MODAL)) {
            try {
                _modal = NodeHelper.getInstance().nodeFromMap(map.getMap(MODAL), getInstanceManager());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public WritableMap getData() {
        WritableMap data = super.getData();
        data.putString(NAME, _title);
        if (_modal != null) {
            data.putMap(MODAL, _modal.getData());
        }
        return data;
    }

    @Override
    public String getTitle() {
        return _title;
    }

    public void setModal(Node modal) {
        _modal = modal;
    }

    public Node getModal() {
        return _modal;
    }
}
