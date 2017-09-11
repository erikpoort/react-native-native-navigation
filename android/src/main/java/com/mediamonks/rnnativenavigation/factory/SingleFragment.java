package com.mediamonks.rnnativenavigation.factory;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.facebook.react.ReactRootView;
import com.mediamonks.rnnativenavigation.data.SingleNode;

import java.util.List;

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

	@Override
	public BaseFragment fragmentForPath(String path)
	{
		return null;
	}

	@Override
	public SingleFragment getCurrentFragment()
	{
		return this;
	}

	public BaseFragment getRootFragment()
	{
		List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
		for (Fragment fragment : fragments)
		{
			BaseFragment baseFragment;
			if (fragment instanceof BaseFragment)
			{
				baseFragment = (BaseFragment) fragment;
				if (baseFragment.isRootFragment())
				{
					return baseFragment;
				}
			}
		}
		return null;
	}
}
