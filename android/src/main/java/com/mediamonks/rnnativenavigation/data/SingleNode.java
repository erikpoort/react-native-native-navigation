package com.mediamonks.rnnativenavigation.data;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.mediamonks.rnnativenavigation.factory.BaseFragment;
import com.mediamonks.rnnativenavigation.factory.NodeHelper;
import com.mediamonks.rnnativenavigation.factory.SingleFragment;

/**
 * Created by erik on 09/08/2017.
 * RNNativeNavigation 2017
 */

public class SingleNode extends BaseNode<SingleFragment> implements Node
{
	public static String JS_NAME = "SingleView";

	private static final String NAME = "name";
	private static final String MODAL = "modal";

	private ReactInstanceManager _instanceManager;
	private String _title;
	private Node _modal;

	@Override
	public BaseFragment<SingleNode> getFragment()
	{
		if (_fragment == null)
		{
			_fragment = new SingleFragment();
			_fragment.setNode(this);
		}
		return _fragment;
	}

	@Override
	public void setInstanceManager(ReactInstanceManager instanceManager)
	{
		_instanceManager = instanceManager;
	}

	public ReactInstanceManager getInstanceManager()
	{
		return _instanceManager;
	}

	@Override
	public void setData(ReadableMap map)
	{
		super.setData(map);
		_title = map.getString(NAME);

		if (map.hasKey(MODAL))
		{
			try
			{
				_modal = NodeHelper.nodeFromMap(map.getMap(MODAL), getInstanceManager());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public WritableMap data()
	{
		WritableMap data = super.data();
		data.putString(NAME, _title);
		if (_modal != null)
		{
			data.putMap(MODAL, _modal.data());
		}
		return data;
	}

	@Override
	public String getTitle()
	{
		return _title;
	}

	public void setModal(Node modal)
	{
		_modal = modal;
	}

	public Node getModal()
	{
		return _modal;
	}
}
