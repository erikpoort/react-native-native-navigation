package com.mediamonks.rnnativenavigation.factory;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.mediamonks.rnnativenavigation.data.Node;

/**
 * Created by erik on 10/08/2017.
 * RNNativeNavigation 2017
 */

public abstract class BaseFragment<T extends Node> extends Fragment
{
	private StackFragment _stackFragment;

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

	public abstract BaseFragment fragmentForPath(String path);

	public void setStackFragment(StackFragment stackFragment)
	{
		_stackFragment = stackFragment;
	}

	public StackFragment getStackFragment()
	{
		return _stackFragment;
	}

	protected String getRootPath()
	{
		int index = getNode().getScreenID().indexOf("/", 1);
		if (index > -1 && index < getNode().getScreenID().length())
		{
			return getNode().getScreenID().substring(0, index);
		}
		else if (index < 0)
		{
			return getNode().getScreenID();
		}
		else
		{
			return "";
		}
	}

	protected boolean isRootFragment()
	{
		return getNode().getScreenID().equals(getRootPath());
	}

	public abstract SingleFragment getCurrentFragment();
}
