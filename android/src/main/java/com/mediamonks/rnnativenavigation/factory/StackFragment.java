package com.mediamonks.rnnativenavigation.factory;

import android.content.Context;
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
import com.mediamonks.rnnativenavigation.bridge.RNNNState;
import com.mediamonks.rnnativenavigation.data.Node;
import com.mediamonks.rnnativenavigation.data.StackNode;

import java.util.Stack;

/**
 * Created by erik on 12/08/2017.
 * RNNativeNavigation 2017
 */

public class StackFragment extends BaseFragment<StackNode>
{
	private FrameLayout _holder;
	private Stack<BaseFragment> _stack;
	private Toolbar _toolbar;
	private Drawable _upIcon;

	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);

		_stack = new Stack<>();
	}

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

		for (Node node : getNode().getStack())
		{
			push(node, false);
		}

		this.handleCurrentStack();

		_toolbar.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onBackPressed();
			}
		});
	}

	public void push(Node node)
	{
		push(node, true);
	}

	public void push(Node node, boolean animated)
	{
		BaseFragment fragment = node.getFragment();
		fragment.setStackFragment(this);
		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		transaction.add(_holder.getId(), fragment);
		transaction.setTransition(animated ? FragmentTransaction.TRANSIT_FRAGMENT_OPEN : FragmentTransaction.TRANSIT_NONE);
		transaction.commit();
		_stack.add(fragment);

		this.handleCurrentStack();
	}

	private void handleCurrentStack()
	{
		int size = _stack.size();
		_toolbar.setNavigationIcon(size > 1 ? _upIcon : null);
		_toolbar.setTitle(_stack.peek().getNode().getTitle());
	}

	@Override
	public boolean onBackPressed()
	{
		if (_holder.getChildCount() > 1)
		{
			FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
			transaction.remove(_stack.pop());
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
			transaction.commit();
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
			BaseFragment checkFragment;
			BaseFragment foundFragment = null;

			int i = 0;
			do
			{
				if (i < this._stack.size())
				{
					checkFragment = _stack.get(i++);

					if (path.indexOf(checkFragment.getNode().getScreenID()) == 0) {
						foundFragment = checkFragment;
					}
				}
				else
				{
					checkFragment = null;
				}
			} while (checkFragment != null);

			if (foundFragment != null) {
				if (!foundFragment.getNode().getScreenID().equals(path)) {
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
		BaseFragment topFragment = _stack.peek();
		if (topFragment instanceof SingleFragment) {
			return (SingleFragment) topFragment;
		}
		return topFragment.getCurrentFragment();
	}

	@Override
	public void onDestroy()
	{
		if (_stack != null)
		{
			while (_stack.size() > 0)
			{
				BaseFragment fragment = _stack.pop();
				fragment.onDestroyView();
			}
		}

		super.onDestroy();
	}
}
