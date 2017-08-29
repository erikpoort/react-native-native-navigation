package com.mediamonks.rnnativenavigation.factory;

import android.app.Fragment;

import com.mediamonks.rnnativenavigation.data.Node;

/**
 * Created by erik on 10/08/2017.
 * RNNativeNavigation 2017
 */

public abstract class BaseFragment<T extends Node> extends Fragment
{
	private T _node;

	public void setNode(T node)
	{
		_node = node;
	}

	public T getNode()
	{
		return _node;
	}

	public boolean onBackPressed()
	{
		return false;
	}
}
