package com.mediamonks.rnnativenavigation.factory;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.mediamonks.rnnativenavigation.data.Node;
import com.mediamonks.rnnativenavigation.data.SingleNode;
import com.mediamonks.rnnativenavigation.react.RNRootView;

import java.util.HashMap;

/**
 * Created by erik on 09/08/2017.
 * RNNativeNavigation 2017
 */
public class SingleFragment extends BaseFragment<SingleNode> implements Navigatable {
	private ModalFragment _modalFragment;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		RNRootView rootView = new RNRootView(getActivity());
		rootView.setBackgroundColor(Color.WHITE);
		rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return rootView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Log.i("MMM onViewCreated", getNode().getScreenID());
		RNRootView rootView = (RNRootView) view;
		rootView.startReactApplication(getNode().getInstanceManager(), getNode().getScreenID());
	}

	@Override public void onResume() {
		super.onResume();
		Log.i("MMM onResume", getNode().getScreenID());
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		Log.i("MMM onDestroyView", getNode().getScreenID());

		invalidate();
	}

	@Override
	public void onStart() {
		super.onStart();

		if (getNode().getModal() != null) {
			showModal(getNode().getModal(), false);
		}
	}

	@Override
	public BaseFragment fragmentForPath(String path) {
		if (path.equals(getNode().getScreenID())) {
			return this;
		}
		if (_modalFragment != null && getNode().getModal() != null) {
			if (path.equals(getNode().getModal().getScreenID())) {
				return _modalFragment.getFragment();
			}
			return _modalFragment.fragmentForPath(path);
		}
		return null;
	}

	@Override public void invalidate() {
		final RNRootView rootView = (RNRootView) getView();
		if (rootView != null) {
			Log.i("MMM", "Clean " + getNode().getScreenID());
			rootView.invalidate();
		}

		if (_modalFragment != null) {
			_modalFragment.invalidate();
		}
	}

	@Override
	public SingleFragment getCurrentFragment() {
		if (getNode().getModal() != null) {
			return getNode().getModal().generateFragment().getCurrentFragment();
		}
		return this;
	}

	@Override public void callMethodWithName(String name, ReadableMap arguments, RNNNFragment rootFragment, Callback callback) {
		switch (name) {
			case SingleNode.SHOW_MODAL: {
				this.handleShowModalCall(arguments, rootFragment, callback);
				break;
			}
			case SingleNode.UPDATE_STYLE: {
				this.handleUpdateStyleCall(arguments);
				break;
			}
		}
	}

	public void handleShowModalCall(final ReadableMap arguments, RNNNFragment rootFragment, Callback callback) {
		try {
			final Node node = NodeHelper.getInstance().nodeFromMap(arguments.getMap("screen"), getNode().getInstanceManager());

			this.getNode().setModal(node);

			callback.invoke(Arguments.makeNativeMap(rootFragment.getNode().getData()));

			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					SingleFragment.this.showModal(node, true);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleUpdateStyleCall(ReadableMap arguments) {
		// Title
		HashMap <String, Object> style = getNode().getStyle().toHashMap();
		if (arguments.hasKey("title")) {
			String title = arguments.getString("title");
			style.put("title", title);
		}
		if (arguments.hasKey("barTint")) {
			int barTint = arguments.getInt("barTint");
			style.put("barTint", barTint);
		}
		getNode().setStyle(Arguments.makeNativeMap(style));

		getStackFragment().callMethodWithName(SingleNode.UPDATE_STYLE, arguments, null, null);
	}

	public void showModal(Node node, boolean animated) {
		_modalFragment = new ModalFragment();
		_modalFragment.setNode(node);
		_modalFragment.setAnimated(animated);
		_modalFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				_modalFragment.setOnDismissListener(null);
				getNode().setModal(null);
			}
		});
		_modalFragment.show(getChildFragmentManager(), node.getScreenID());
	}
}
