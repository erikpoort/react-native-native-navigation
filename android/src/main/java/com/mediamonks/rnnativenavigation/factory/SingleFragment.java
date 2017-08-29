package com.mediamonks.rnnativenavigation.factory;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

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
		super.onCreateView(inflater, container, savedInstanceState);
		ReactRootView rootView = new ReactRootView(getActivity());
		rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		rootView.startReactApplication(this.getNode().getInstanceManager(), this.getNode().getData().getString("screenID"));
		return rootView;
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
}
