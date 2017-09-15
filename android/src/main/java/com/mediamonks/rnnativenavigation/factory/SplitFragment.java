package com.mediamonks.rnnativenavigation.factory;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mediamonks.rnnativenavigation.data.SplitNode;

/**
 * Created by erik on 15/09/2017.
 * example 2017
 */

public class SplitFragment extends BaseFragment<SplitNode>
{
	private BaseFragment _fragment1;
	private BaseFragment _fragment2;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		LinearLayout linearLayout = new LinearLayout(getContext());
		linearLayout.setOrientation(getNode().getAxis());
		linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		// I'm calling generateViewId() here, because calling it the first doesn't work on first load. My assumption is the initial id is later hijacked by ReactNative, making it impossible to add fragments
		View.generateViewId();

		_fragment1 = getNode().getNode1().getFragment();
		FrameLayout frameLayout1 = new FrameLayout(getContext());
		frameLayout1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
		frameLayout1.setId(View.generateViewId());
		linearLayout.addView(frameLayout1);

		_fragment2 = getNode().getNode2().getFragment();
		FrameLayout frameLayout2 = new FrameLayout(getContext());
		frameLayout2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
		frameLayout2.setId(View.generateViewId());
		linearLayout.addView(frameLayout2);

		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction1 = fragmentManager.beginTransaction();
		transaction1.add(frameLayout1.getId(), _fragment1);
		transaction1.commit();

		FragmentTransaction transaction2 = fragmentManager.beginTransaction();
		transaction2.add(frameLayout2.getId(), _fragment2);
		transaction2.commit();

		return linearLayout;
	}

	@Override
	public BaseFragment fragmentForPath(String path)
	{
		if (path.indexOf(getNode().getScreenID()) == 0)
		{
			BaseFragment foundFragment = null;
			if (path.indexOf(getNode().getNode1().getScreenID()) == 0)
			{
				foundFragment = _fragment1;
			}
			else if (path.indexOf(getNode().getNode2().getScreenID()) == 0)
			{
				foundFragment = _fragment2;
			}
			if (foundFragment != null && !foundFragment.getNode().getScreenID().equals(path))
			{
				foundFragment = foundFragment.fragmentForPath(path);
			}
			return foundFragment;
		}
		return null;
	}

	@Override
	public SingleFragment getCurrentFragment()
	{
		return _fragment1.getCurrentFragment();
	}
}
