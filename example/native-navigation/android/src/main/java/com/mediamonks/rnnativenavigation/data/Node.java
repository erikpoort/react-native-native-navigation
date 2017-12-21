package com.mediamonks.rnnativenavigation.data;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.mediamonks.rnnativenavigation.factory.BaseFragment;

import java.io.Serializable;

/**
 * Created by erik on 09/08/2017.
 * RNNativeNavigation 2017
 */

public interface Node extends Serializable {
    void setInstanceManager(ReactInstanceManager instanceManager);

    void setData(ReadableMap map);

    WritableMap data();

    void setShown(boolean shown);

    boolean isShown();

    String getTitle();

    String getScreenID();

    BaseFragment generateFragment();

    String getRootPath();
}
