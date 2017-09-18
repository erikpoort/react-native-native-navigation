package com.mediamonks.rnnativenavigation.factory;

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
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		LinearLayout linearLayout = new LinearLayout(getContext());
		linearLayout.setOrientation(getNode().getAxis());
		linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		// I'm calling generateViewId() here, because calling it the first doesn't work on first load. My assumption is the initial id is later hijacked by ReactNative, making it impossible to add fragments
		View.generateViewId();

		FrameLayout frameLayout1 = new FrameLayout(getContext());
		frameLayout1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
		frameLayout1.setId(View.generateViewId());
		linearLayout.addView(frameLayout1);

		FrameLayout frameLayout2 = new FrameLayout(getContext());
		frameLayout2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
		frameLayout2.setId(View.generateViewId());
		linearLayout.addView(frameLayout2);

		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction1 = fragmentManager.beginTransaction();
		transaction1.add(frameLayout1.getId(), getNode().getNode1().getFragment());
		transaction1.commit();

		FragmentTransaction transaction2 = fragmentManager.beginTransaction();
		transaction2.add(frameLayout2.getId(), getNode().getNode2().getFragment());
		transaction2.commit();

		return linearLayout;
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();

		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

		FragmentTransaction transaction1 = fragmentManager.beginTransaction();
		transaction1.remove(getNode().getNode1().getFragment());
		transaction1.commit();

		FragmentTransaction transaction2 = fragmentManager.beginTransaction();
		transaction2.remove(getNode().getNode2().getFragment());
		transaction2.commit();
	}

	@Override
	public BaseFragment fragmentForPath(String path)
	{
		if (path.indexOf(getNode().getScreenID()) == 0)
		{
			BaseFragment foundFragment = null;
			if (path.indexOf(getNode().getNode1().getScreenID()) == 0)
			{
				foundFragment = getNode().getNode1().getFragment();
			}
			else if (path.indexOf(getNode().getNode2().getScreenID()) == 0)
			{
				foundFragment = getNode().getNode2().getFragment();
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
		return getNode().getNode1().getFragment().getCurrentFragment();
	}
}
