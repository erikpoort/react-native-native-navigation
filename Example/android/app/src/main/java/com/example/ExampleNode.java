package com.example;

import com.mediamonks.rnnativenavigation.data.SingleNode;
import com.mediamonks.rnnativenavigation.factory.BaseFragment;

/**
 * Created by erik on 05/01/2018.
 * android 2018
 */

public class ExampleNode extends SingleNode {
    public static String JS_NAME = "ExampleView";

    @Override public BaseFragment<SingleNode> generateFragment() {
        BaseFragment<SingleNode> fragment = super.generateFragment();
        return fragment;
    }
}
