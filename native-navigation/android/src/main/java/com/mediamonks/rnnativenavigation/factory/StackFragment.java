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

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.mediamonks.rnnativenavigation.R;
import com.mediamonks.rnnativenavigation.data.Node;
import com.mediamonks.rnnativenavigation.data.StackNode;

import java.util.Stack;

/**
 * Created by erik on 12/08/2017.
 * RNNativeNavigation 2017
 */

public class StackFragment extends BaseFragment<StackNode> implements Navigatable {
	private FrameLayout _holder;
	private Toolbar _toolbar;
	private Drawable _upIcon;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// I'm calling generateViewId() twice, calling it once doesn't work on first load. My assumption is the initial id is later hijacked by ReactNative, making it impossible to add fragments
		View.generateViewId();

		LinearLayout linearLayout = new LinearLayout(getActivity());
		linearLayout.setBackgroundColor(Color.WHITE);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		_toolbar = (Toolbar) inflater.inflate(R.layout.toolbar, linearLayout, false);
		_upIcon = _toolbar.getNavigationIcon();
		TypedValue typedValue = new TypedValue();
		if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
			_toolbar.setLayoutParams(new Toolbar.LayoutParams(LayoutParams.MATCH_PARENT, TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics())));
		}
		linearLayout.addView(_toolbar);

		_holder = new FrameLayout(getActivity());

		_holder.setId(View.generateViewId());
		linearLayout.addView(_holder, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1));

		return linearLayout;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	@Override public void onDestroyView() {
		super.onDestroyView();

		int leni = getNode().getStack().size();
		for (int i = leni - 1; i > 0; --i) {
			Node node = getNode().getStack().get(i);
			removeNode(node, FragmentTransaction.TRANSIT_NONE);
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		showPeek(FragmentTransaction.TRANSIT_NONE);
	}

	@Override public void callMethodWithName(String name, ReadableMap arguments, RNNNFragment rootFragment, Callback callback) {
		switch (name) {
			case StackNode.PUSH: {
				this.handlePushCall(arguments, rootFragment, callback);
				break;
			}
			case StackNode.POP: {
				this.handlePopCall(rootFragment, callback);
				break;
			}
			case StackNode.POP_TO_ROOT: {
				this.handlePopToRootCall(arguments, rootFragment, callback);
				break;
			}
		}
	}

	private void handlePushCall(final ReadableMap arguments, final RNNNFragment rootFragment, final Callback callback) {
		final Node node;
		try {
			node = NodeHelper.getInstance().nodeFromMap(arguments.getMap("screen"), getNode().getInstanceManager());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		final boolean reset = arguments.getMap("arguments").getBoolean("reset");

		Stack<Node> stack = getNode().getStack();
		if (reset) {
			stack.removeAllElements();
		}
		stack.add(node);

		callback.invoke(Arguments.makeNativeMap(rootFragment.getNode().getData().toHashMap()));

		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (reset) {
					while (getNode().getStack().size() > 1) {
						removeNode(getNode().getStack().pop(), FragmentTransaction.TRANSIT_NONE);
					}
				}

				StackFragment.this.pushNode(node, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			}
		});
	}

	private void handlePopCall(RNNNFragment rootFragment, final Callback callback) {
		try {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (getNode().getStack().size() > 1) {
						popNode(getNode().getStack().peek(), FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
					}
				}
			});

			callback.invoke(Arguments.makeNativeMap(rootFragment.getNode().getData().toHashMap()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handlePopToRootCall(ReadableMap arguments, RNNNFragment rootFragment, final Callback callback) {
		try {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					popNode(getNode().getStack().peek(), FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
					while (getNode().getStack().size() > 1) {
						popNode(getNode().getStack().peek(), FragmentTransaction.TRANSIT_NONE);
					}
				}
			});

			callback.invoke(Arguments.makeNativeMap(rootFragment.getNode().getData().toHashMap()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showPeek(int transition) {
		Node node = getNode().getStack().peek();
		pushNode(node, transition);
	}

	public void pushNode(Node node, int transition) {
		BaseFragment fragment = node.generateFragment();
		fragment.setStackFragment(this);
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		transaction.add(_holder.getId(), fragment, node.getScreenID());
		transaction.setTransition(transition);
		transaction.commitNowAllowingStateLoss();
		node.setShown(true);

		handleCurrentStack();
	}

	private void removeNode(Node node, int transition) {
		if (node.isShown()) {
			FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
			String topID = node.getScreenID();
			transaction.remove(getChildFragmentManager().findFragmentByTag(topID));
			transaction.setTransition(transition);
			transaction.commitNowAllowingStateLoss();
			node.setShown(false);
		}
	}

	public void popNode(Node node, int transition) {
		removeNode(node, transition);
		getNode().getStack().remove(getNode().getStack().peek());

		if (!getNode().getStack().peek().isShown()) {
			showPeek(FragmentTransaction.TRANSIT_NONE);
		}

		handleCurrentStack();
	}

	private void handleCurrentStack() {
		int size = getNode().getStack().size();
		_toolbar.setNavigationIcon(size > 1 ? _upIcon : null);
		_toolbar.setTitle(getNode().getStack().peek().getTitle());
	}

	@Override
	public boolean onBackPressed() {
		if (getNode().getStack().size() > 1) {
			popNode(getNode().getStack().peek(), FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
			return true;
		}
		return false;
	}

	@Override
	public BaseFragment fragmentForPath(String path) {
		if (path.equals(getNode().getScreenID())) {
			return this;
		}
		if (path.indexOf(getNode().getScreenID()) == 0) {

			BaseFragment foundFragment = null;
			Node checkNode;

			int i = 0;
			do {
				if (i < getNode().getStack().size()) {
					checkNode = getNode().getStack().get(i++);

					if (path.indexOf(checkNode.getScreenID()) == 0) {
						foundFragment = (BaseFragment) getChildFragmentManager().findFragmentByTag(checkNode.getScreenID());
					}
				} else {
					checkNode = null;
				}
			}
			while (checkNode != null);

			if (foundFragment != null) {
				if (!foundFragment.getNode().getScreenID().equals(path)) {
					foundFragment = foundFragment.fragmentForPath(path);
				}

				return foundFragment;
			}
		}
		return null;
	}

	@Override public void invalidate() {
		for (Node node : getNode().getStack()) {
			BaseFragment fragment = (BaseFragment) getChildFragmentManager().findFragmentByTag(node.getScreenID());
			if (fragment != null) {
				fragment.invalidate();
			}
		}
	}

	@Override
	public SingleFragment getCurrentFragment() {
		String topID = getNode().getStack().peek().getScreenID();
		BaseFragment topFragment = (BaseFragment) getChildFragmentManager().findFragmentByTag(topID);
		if (topFragment instanceof SingleFragment) {
			return (SingleFragment) topFragment;
		}
		return topFragment.getCurrentFragment();
	}
}
