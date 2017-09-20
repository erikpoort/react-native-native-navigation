package com.mediamonks.rnnativenavigation.factory;

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
public class SingleFragment extends BaseFragment<SingleNode>
{
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		ReactRootView rootView = new ReactRootView(getActivity());
		rootView.setBackgroundColor(Color.WHITE);
		rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return rootView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		ReactRootView rootView = (ReactRootView) view;
		rootView.startReactApplication(getNode().getInstanceManager(), getNode().getScreenID());
	}

	@Override
	public void onDestroyView()
	{
		ReactRootView rootView = (ReactRootView) getView();
		if (rootView != null)
		{
			rootView.unmountReactApplication();
		}
		super.onDestroyView();
	}

	@Override
	public void onStart()
	{
		super.onStart();

		Log.i("MMM onStart", getNode().getScreenID());
	}

	@Override
	public BaseFragment fragmentForPath(String path)
	{
		if (getNode().getModal() != null)
		{
			String modalScreenID = getNode().getModal().getScreenID();
			if (modalScreenID.equals(path))
			{
				return getNode().getModal().getFragment();
			}
			else if (path.contains(modalScreenID))
			{
				return getNode().getModal().getFragment().fragmentForPath(path);
			}
		}
		return this;
	}

	@Override
	public SingleFragment getCurrentFragment()
	{
		if (getNode().getModal() != null)
		{
			return getNode().getModal().getFragment().getCurrentFragment();
		}
		return this;
	}

	public void showModal(Node node)
	{
		ModalFragment modalFragment = new ModalFragment();
		modalFragment.setNode(node);
		modalFragment.show(getActivity().getSupportFragmentManager(), node.getScreenID());
	}
}
