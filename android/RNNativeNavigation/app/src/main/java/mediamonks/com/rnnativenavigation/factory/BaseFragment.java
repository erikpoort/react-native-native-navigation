package mediamonks.com.rnnativenavigation.factory;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import mediamonks.com.rnnativenavigation.data.Node;

/**
 * Created by erik on 10/08/2017.
 * RNNativeNavigation 2017
 */

public abstract class BaseFragment<T extends Node> extends Fragment
{
	private T _node;

	protected BaseActivity getBaseActivity()
	{
		return (BaseActivity) getActivity();
	}

	public void setNode(T node)
	{
		_node = node;
	}

	public T getNode()
	{
		return _node;
	}
}
