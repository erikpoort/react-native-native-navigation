package com.mediamonks.rnnativenavigation.factory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mediamonks.rnnativenavigation.data.DrawerNode;

/**
 * Created by erik on 18/09/2017.
 * example 2017
 */

public class DrawerFragment extends BaseFragment<DrawerNode>
{
	private BaseFragment _centerFragment;
	private BaseFragment _leftFragment;
	private BaseFragment _rightFragment;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		// I'm calling generateViewId() here, because calling it the first doesn't work on first load. My assumption is the initial id is later hijacked by ReactNative, making it impossible to add fragments
		View.generateViewId();

		final DrawerLayout drawerLayout = new DrawerLayout(getContext());
		drawerLayout.setId(View.generateViewId());

		FragmentManager fragmentManager = getChildFragmentManager();

		FrameLayout centerLayout = new FrameLayout(getContext());
		centerLayout.setId(View.generateViewId());
		drawerLayout.addView(centerLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		FragmentTransaction centerTransaction = fragmentManager.beginTransaction();
		_centerFragment = getNode().getCenterNode().generateFragment();
		centerTransaction.replace(centerLayout.getId(), _centerFragment);
		centerTransaction.commit();

		if (getNode().getLeftNode() != null)
		{
			FrameLayout leftLayout = new FrameLayout(getContext());
			DrawerLayout.LayoutParams leftParams = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			leftParams.gravity = Gravity.START;
			leftLayout.setLayoutParams(leftParams);
			leftLayout.setId(View.generateViewId());
			drawerLayout.addView(leftLayout);

			FragmentTransaction leftTransaction = fragmentManager.beginTransaction();
			_leftFragment = getNode().getLeftNode().generateFragment();
			leftTransaction.replace(leftLayout.getId(), _leftFragment);
			leftTransaction.commit();
		}

		if (getNode().getRightNode() != null)
		{
			FrameLayout rightLayout = new FrameLayout(getContext());
			DrawerLayout.LayoutParams rightParams = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			rightParams.gravity = Gravity.END;
			rightLayout.setLayoutParams(rightParams);
			rightLayout.setId(View.generateViewId());
			drawerLayout.addView(rightLayout);

			FragmentTransaction rightTransaction = fragmentManager.beginTransaction();
			_rightFragment = getNode().getRightNode().generateFragment();
			rightTransaction.replace(rightLayout.getId(), _rightFragment);
			rightTransaction.commit();
		}

		if (getNode().getSide() != Gravity.NO_GRAVITY)
		{
			drawerLayout.openDrawer(getNode().getSide(), false);
		}

		drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener()
		{
			@Override
			public void onDrawerOpened(View drawerView)
			{
				super.onDrawerOpened(drawerView);

				int side = Gravity.NO_GRAVITY;
				side = drawerLayout.isDrawerOpen(Gravity.START) ? Gravity.START : side;
				side = drawerLayout.isDrawerOpen(Gravity.END) ? Gravity.END : side;
				getNode().setSide(side);
			}

			@Override
			public void onDrawerClosed(View drawerView)
			{
				super.onDrawerClosed(drawerView);

				getNode().setSide(Gravity.CENTER);
			}
		});

		return drawerLayout;
	}

	@Override
	public void onDestroyView()
	{
		FragmentManager fragmentManager = getChildFragmentManager();

		if (getNode().getLeftNode() != null)
		{
			FragmentTransaction leftTransaction = fragmentManager.beginTransaction();
			leftTransaction.remove(_leftFragment);
			leftTransaction.commit();
		}

		FragmentTransaction centerTransaction = fragmentManager.beginTransaction();
		centerTransaction.remove(_centerFragment);
		centerTransaction.commit();

		if (getNode().getRightNode() != null)
		{
			FragmentTransaction rightTransaction = fragmentManager.beginTransaction();
			rightTransaction.remove(_rightFragment);
			rightTransaction.commit();
		}

		super.onDestroyView();
	}

	@Override
	public BaseFragment fragmentForPath(String path)
	{
		if (path.indexOf(getNode().getScreenID()) == 0)
		{
			BaseFragment foundFragment = null;

			if (_leftFragment != null && path.indexOf(_leftFragment.getNode().getScreenID()) == 0)
			{
				foundFragment = _leftFragment;
			}
			else if (_centerFragment != null && path.indexOf(_centerFragment.getNode().getScreenID()) == 0)
			{
				foundFragment = _centerFragment;
			}
			else if (_rightFragment != null && path.indexOf(_rightFragment.getNode().getScreenID()) == 0)
			{
				foundFragment = _rightFragment;
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
		return _centerFragment.getCurrentFragment();
	}
}
