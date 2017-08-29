package com.mediamonks.rnnativenavigation.factory;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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
	private Stack<Fragment> _stack;
	private Toolbar _toolbar;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		_stack = new Stack<>();

		LinearLayout linearLayout = new LinearLayout(getActivity());
		linearLayout.setBackgroundColor(Color.WHITE);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		_toolbar = new Toolbar(getActivity());
		TypedValue typedValue = new TypedValue();
		if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true))
		{
			_toolbar.setLayoutParams(new Toolbar.LayoutParams(LayoutParams.MATCH_PARENT, TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics())));
		}
		_toolbar.setTitle(getNode().getData().getString("name"));
		linearLayout.addView(_toolbar);

		_holder = new FrameLayout(getActivity());
		_holder.setId(View.generateViewId());
		linearLayout.addView(_holder, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1));

		for (Node node : getNode().getStack())
		{
			Fragment fragment = node.getFragment();
			FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
			transaction.add(_holder.getId(), fragment);
			transaction.commit();
			_stack.add(fragment);
		}

		this.handleCurrentStack();

		return linearLayout;
	}

	private void handleCurrentStack()
	{
		int size = _stack.size();
		String title = getNode().getStack().get(size - 1).getData().getString("screenID");
		_toolbar.setTitle(title);
	}

	@Override
	public boolean onBackPressed()
	{
		if (_holder.getChildCount() > 1)
		{
			FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
			transaction.remove(_stack.pop());
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
			transaction.commit();
			this.handleCurrentStack();
			return true;
		}
		return false;
	}

	@Override
	public void onDestroy()
	{
		if (_stack != null) {
			for (Fragment fragment : _stack) {
				onBackPressed();
			}
		}

		super.onDestroy();
	}
}
