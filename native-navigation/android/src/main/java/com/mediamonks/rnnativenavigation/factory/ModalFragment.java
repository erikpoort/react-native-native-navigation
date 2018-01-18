package com.mediamonks.rnnativenavigation.factory;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.facebook.infer.annotation.Assertions;
import com.mediamonks.rnnativenavigation.R;
import com.mediamonks.rnnativenavigation.data.Node;

/**
 * Created by erik on 19/09/2017.
 * example 2017
 */

public class ModalFragment extends DialogFragment implements RNNNFragment {
    private Node _node;
    private DialogInterface.OnDismissListener _onDismissListener;
    private BaseFragment _fragment;

    private View.OnKeyListener _forwardKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            switch (keyCode) {
                // Ignore all these keys, as they are the ones that PhoneWindow cares about
                case KeyEvent.KEYCODE_VOLUME_UP:
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                case KeyEvent.KEYCODE_VOLUME_MUTE:
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                case KeyEvent.KEYCODE_MUTE:
                case KeyEvent.KEYCODE_HEADSETHOOK:
                case KeyEvent.KEYCODE_MEDIA_STOP:
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                case KeyEvent.KEYCODE_MEDIA_REWIND:
                case KeyEvent.KEYCODE_MEDIA_RECORD:
                case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                case KeyEvent.KEYCODE_MENU:
                case KeyEvent.KEYCODE_BACK:
                case KeyEvent.KEYCODE_SEARCH:
                case KeyEvent.KEYCODE_WINDOW:
                    return false;
                default:
                    getActivity().onKeyUp(keyCode, event);
                    return true;
            }
        }
    };

    private ViewTreeObserver.OnGlobalFocusChangeListener _globalFocusChangeListener = new ViewTreeObserver.OnGlobalFocusChangeListener() {
        @Override
        public void onGlobalFocusChanged(View oldFocus, View newFocus) {
            if (oldFocus != null) {
                oldFocus.setOnKeyListener(null);
            }
            if (newFocus != null) {
                // DialogsFragment views don't bubble alphanumeric key events up to the containing Activity, because their containing PhoneVWindow discards them.
                // Here we forward them forcefully in order to allow development shortcuts on the emulator
                newFocus.setOnKeyListener(_forwardKeyListener);
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout view = new FrameLayout(getContext());
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        View.generateViewId();
        view.setId(View.generateViewId());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        _fragment = getNode().generateFragment();
        transaction.add(getView().getId(), _fragment);
        transaction.commitNowAllowingStateLoss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (_onDismissListener != null) {
            _onDismissListener.onDismiss(dialog);
        }
    }

    @Override
    public Node getNode() {
        return _node;
    }

    public void setNode(Node node) {
        _node = node;
    }

    @Override
    public BaseFragment fragmentForPath(String path) {
        return _fragment.fragmentForPath(path);
    }

    @Override public void invalidate() {
        if (_fragment != null) {
            _fragment.invalidate();
        }
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        _onDismissListener = onDismissListener;
    }
}
