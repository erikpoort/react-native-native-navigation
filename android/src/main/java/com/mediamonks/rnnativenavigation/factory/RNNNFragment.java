package com.mediamonks.rnnativenavigation.factory;

import com.mediamonks.rnnativenavigation.data.Node;

/**
 * Created by erik on 19/09/2017.
 * example 2017
 */

public interface RNNNFragment {
    Node getNode();

    BaseFragment fragmentForPath(String path);
}
