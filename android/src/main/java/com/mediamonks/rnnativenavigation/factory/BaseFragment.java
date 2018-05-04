package com.mediamonks.rnnativenavigation.factory;

import android.support.v4.app.Fragment;

import com.mediamonks.rnnativenavigation.data.Node;

/**
 * Created by erik on 10/08/2017.
 * RNNativeNavigation 2017
 */

public abstract class BaseFragment<T extends Node> extends Fragment implements RNNNFragment {
	private StackFragment _stackFragment;
	private ModalFragment _modalFragment;

	private T _node;

	public void setNode(T node) {
		_node = node;
	}

	public T getNode() {
		return _node;
	}

	public boolean onBackPressed() {
		return false;
	}

	public abstract BaseFragment fragmentForPath(String path);

	public void setStackFragment(StackFragment stackFragment) {
		_stackFragment = stackFragment;
	}

	public StackFragment getStackFragment() {
		return _stackFragment;
	}

	public void setModalFragment(ModalFragment modalFragment) {
		_modalFragment = modalFragment;
	}

	public ModalFragment getModalFragment() {
		return _modalFragment;
	}

	public abstract SingleFragment getCurrentFragment();
}
