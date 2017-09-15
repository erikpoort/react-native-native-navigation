package com.mediamonks.rnnativenavigation.factory;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mediamonks.rnnativenavigation.R;
import com.mediamonks.rnnativenavigation.data.Node;
import com.mediamonks.rnnativenavigation.data.StackNode;

/**
 * Created by erik on 12/08/2017.
 * RNNativeNavigation 2017
 */

public class StackFragment extends BaseFragment<StackNode>
{
	private FrameLayout _holder;
	private Toolbar _toolbar;
	private Drawable _upIcon;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		LinearLayout linearLayout = new LinearLayout(getActivity());
		linearLayout.setBackgroundColor(Color.WHITE);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		_toolbar = (Toolbar) inflater.inflate(R.layout.toolbar, linearLayout, false);
		_upIcon = _toolbar.getNavigationIcon();
		TypedValue typedValue = new TypedValue();
		if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true))
		{
			_toolbar.setLayoutParams(new Toolbar.LayoutParams(LayoutParams.MATCH_PARENT, TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics())));
		}
		linearLayout.addView(_toolbar);

		_holder = new FrameLayout(getActivity());
		// I'm calling generateViewId() twice, calling it once doesn't work on first load. My assumption is the initial id is later hijacked by ReactNative, making it impossible to add fragments
		View.generateViewId();
		int id = View.generateViewId();
		_holder.setId(id);
		linearLayout.addView(_holder, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1));

		return linearLayout;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		showPeek(FragmentTransaction.TRANSIT_NONE);

		_toolbar.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onBackPressed();
			}
		});
	}

	private void showPeek(int transition)
	{
		Node node = getNode().getStack().peek();
		BaseFragment fragment = node.getFragment();
		fragment.setStackFragment(this);
		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		transaction.add(_holder.getId(), fragment);
		transaction.setTransition(transition);
		transaction.commit();
		node.setShown(true);
	}

	public void push()
	{
		showPeek(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		handleCurrentStack();
	}

	private void handleCurrentStack()
	{
		int size = getNode().getStack().size();
		_toolbar.setNavigationIcon(size > 1 ? _upIcon : null);
		_toolbar.setTitle(getNode().getStack().peek().getTitle());
	}

	@Override
	public boolean onBackPressed()
	{
		if (getNode().getStack().size() > 1)
		{
			FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
			transaction.remove(getNode().getStack().pop().getFragment());
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
			transaction.commit();

			if (!getNode().getStack().peek().isShown())
			{
				showPeek(FragmentTransaction.TRANSIT_NONE);
			}

			this.handleCurrentStack();

			return true;
		}
		return false;
	}

	@Override
	public BaseFragment fragmentForPath(String path)
	{
		if (path.indexOf(getNode().getScreenID()) == 0)
		{
			BaseFragment foundFragment = null;
			Node checkNode;

			int i = 0;
			do
			{
				if (i < getNode().getStack().size())
				{
					checkNode = getNode().getStack().get(i++);

					if (path.indexOf(checkNode.getScreenID()) == 0)
					{
						foundFragment = checkNode.getFragment();
					}
				}
				else
				{
					checkNode = null;
				}
			}
			while (checkNode != null);

			if (foundFragment != null)
			{
				if (!foundFragment.getNode().getScreenID().equals(path))
				{
					foundFragment = foundFragment.fragmentForPath(path);
				}

				return foundFragment;
			}
		}
		return null;
	}

	@Override
	public SingleFragment getCurrentFragment()
	{
		BaseFragment topFragment = getNode().getStack().peek().getFragment();
		if (topFragment instanceof SingleFragment)
		{
			return (SingleFragment) topFragment;
		}
		return topFragment.getCurrentFragment();
	}

	@Override
	public void onDestroy()
	{
		if (getNode().getStack() != null)
		{
			while (getNode().getStack().size() > 0)
			{
				Node node = getNode().getStack().pop();
				if (node.isShown())
				{
					node.getFragment().onDestroyView();
				}
			}
		}

		super.onDestroy();
	}
}
