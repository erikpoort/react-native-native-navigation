package com.mediamonks.rnnativenavigation.factory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.facebook.react.ReactRootView;
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
		rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return rootView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		ReactRootView rootView = (ReactRootView) view;
		rootView.startReactApplication(this.getNode().getInstanceManager(), this.getNode().getScreenID());
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
}
