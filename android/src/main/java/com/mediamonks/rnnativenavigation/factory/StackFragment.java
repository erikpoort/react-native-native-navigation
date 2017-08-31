package com.mediamonks.rnnativenavigation.factory;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.internal.app.ToolbarActionBar;
import android.support.v7.widget.Toolbar;
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
	private Stack<BaseFragment> _stack;
	private Toolbar _toolbar;
	private ToolbarActionBar _actionBar;

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

		_toolbar = new Toolbar(getActivity());
		_actionBar = new ToolbarActionBar(_toolbar, "", getActivity());
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
			BaseFragment fragment = node.getFragment();
			FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
			transaction.add(_holder.getId(), fragment);
			transaction.commit();
			_stack.add(fragment);
		}

		this.handleCurrentStack();

		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	private void handleCurrentStack()
	{
		int size = _stack.size();
		_actionBar.setDisplayHomeAsUpEnabled(size > 1);
		_actionBar.setTitle(_stack.peek().getNode().getTitle());
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
	public void onDestroy()
	{
		if (_stack != null)
		{
			while (_stack.size() > 0)
			{
				_stack.pop();
			}
		}

		super.onDestroy();
	}
}
