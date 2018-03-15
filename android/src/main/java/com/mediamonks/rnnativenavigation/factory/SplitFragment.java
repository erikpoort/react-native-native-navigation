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

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.mediamonks.rnnativenavigation.data.Node;
import com.mediamonks.rnnativenavigation.data.SplitNode;

/**
 * Created by erik on 15/09/2017.
 * example 2017
 */

public class SplitFragment extends BaseFragment<SplitNode> implements Navigatable {

	private BaseFragment _fragment1;
	private BaseFragment _fragment2;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

		FragmentManager fragmentManager = getChildFragmentManager();
		FragmentTransaction transaction1 = fragmentManager.beginTransaction();
		_fragment1 = getNode().getNode1().generateFragment();
		transaction1.add(frameLayout1.getId(), _fragment1, getNode().getNode1().getScreenID());
		transaction1.commitNowAllowingStateLoss();

		FragmentTransaction transaction2 = fragmentManager.beginTransaction();
		_fragment2 = getNode().getNode2().generateFragment();
		transaction2.add(frameLayout2.getId(), _fragment2, getNode().getNode2().getScreenID());
		transaction2.commitNowAllowingStateLoss();

		return linearLayout;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		FragmentManager fragmentManager = getChildFragmentManager();

		FragmentTransaction transaction1 = fragmentManager.beginTransaction();
		transaction1.remove(_fragment1);
		transaction1.commitNowAllowingStateLoss();

		FragmentTransaction transaction2 = fragmentManager.beginTransaction();
		transaction2.remove(_fragment2);
		transaction2.commitNowAllowingStateLoss();
	}

	@Override public void callMethodWithName(String name, ReadableMap arguments, RNNNFragment rootFragment, Callback callback) {
		switch (name) {
			case SplitNode.REPLACE: {
				this.handleReplaceCall(arguments, rootFragment, callback);
				break;
			}
		}
	}

	private void handleReplaceCall(ReadableMap arguments, RNNNFragment rootFragment, Callback callback) {
		try {
			final Node node = NodeHelper.getInstance().nodeFromMap(arguments.getMap("screen"), getNode().getInstanceManager());

			String newPart = node.getScreenID().substring(getNode().getScreenID().length() + 1);
			String[] parts = newPart.split("/");
			final String whichNode = parts[0];
			final boolean firstPart = whichNode.equals("node1");

			if (firstPart) {
				getNode().setNode1(node);
			} else {
				getNode().setNode2(node);
			}

			callback.invoke(Arguments.makeNativeMap(rootFragment.getNode().getData()));

			FragmentManager fragmentManager = getChildFragmentManager();
			final FragmentTransaction transaction = fragmentManager.beginTransaction();
			final BaseFragment newFragment = node.generateFragment();

			getActivity().runOnUiThread(new Runnable() {
				@Override public void run() {
					if (firstPart) {
						transaction.replace(_fragment1.getId(), newFragment);
						_fragment1 = newFragment;
					} else {
						transaction.replace(_fragment2.getId(), newFragment);
						_fragment2 = newFragment;
					}

					transaction.commitNowAllowingStateLoss();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public BaseFragment fragmentForPath(String path) {
		if (path.equals(getNode().getScreenID())) {
			return this;
		}
		if (path.indexOf(getNode().getScreenID()) == 0) {
			BaseFragment foundFragment = null;
			if (path.indexOf(getNode().getNode1().getScreenID()) == 0) {
				foundFragment = _fragment1;
			} else if (path.indexOf(getNode().getNode2().getScreenID()) == 0) {
				foundFragment = _fragment2;
			}
			if (foundFragment != null && !foundFragment.getNode().getScreenID().equals(path)) {
				foundFragment = foundFragment.fragmentForPath(path);
			}
			return foundFragment;
		}
		return null;
	}

	@Override public void invalidate() {
		if (_fragment1 != null) {
			_fragment1.invalidate();
		}
		if (_fragment2 != null) {
			_fragment2.invalidate();
		}
	}

	@Override
	public SingleFragment getCurrentFragment() {
		return _fragment1.getCurrentFragment();
	}
}
