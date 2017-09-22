package com.mediamonks.rnnativenavigation.factory;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.facebook.react.ReactRootView;
import com.mediamonks.rnnativenavigation.data.Node;
import com.mediamonks.rnnativenavigation.data.SingleNode;

/**
 * Created by erik on 09/08/2017.
 * RNNativeNavigation 2017
 */
public class SingleFragment extends BaseFragment<SingleNode> {
    private ModalFragment _modalFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ReactRootView rootView = new ReactRootView(getActivity());
        rootView.setBackgroundColor(Color.WHITE);
        rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("MMM onViewCreated", getNode().getScreenID());
        ReactRootView rootView = (ReactRootView) view;
        rootView.startReactApplication(getNode().getInstanceManager(), getNode().getScreenID());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.i("MMM onDestroyView", getNode().getScreenID());

        invalidate();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getNode().getModal() != null) {
            showModal(getNode().getModal());
        }
    }

    @Override
    public BaseFragment fragmentForPath(String path) {
        if (getNode().getModal() != null) {
            return _modalFragment.fragmentForPath(path);
        }
        return this;
    }

    @Override public void invalidate() {
        final ReactRootView rootView = (ReactRootView) getView();
        if (rootView != null) {
            Log.i("MMM", "Clean " + getNode().getScreenID());
            rootView.unmountReactApplication();
        }
    }

    @Override
    public SingleFragment getCurrentFragment() {
        if (getNode().getModal() != null) {
            return getNode().getModal().generateFragment().getCurrentFragment();
        }
        return this;
    }

    public void showModal(Node node) {
        _modalFragment = new ModalFragment();
        _modalFragment.setNode(node);
        _modalFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                _modalFragment.setOnDismissListener(null);
                getNode().setModal(null);
            }
        });
        _modalFragment.show(getChildFragmentManager(), node.getScreenID());
    }
}
