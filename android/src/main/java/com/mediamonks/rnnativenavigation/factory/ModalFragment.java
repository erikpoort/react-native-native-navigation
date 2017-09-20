package com.mediamonks.rnnativenavigation.factory;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mediamonks.rnnativenavigation.R;
import com.mediamonks.rnnativenavigation.data.Node;

/**
 * Created by erik on 19/09/2017.
 * example 2017
 */

public class ModalFragment extends DialogFragment implements RNNNFragment
{
	private Node _node;
	private DialogInterface.OnDismissListener _onDismissListener;

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		FrameLayout view = new FrameLayout(getContext());
		view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		View.generateViewId();
		view.setId(View.generateViewId());

		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		FragmentManager fragmentManager = getChildFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = getNode().getFragment();
		transaction.add(getView().getId(), fragment);
		transaction.commit();
	}

	@Override
	public void onDismiss(DialogInterface dialog)
	{
		super.onDismiss(dialog);

		if (_onDismissListener != null)
		{
			_onDismissListener.onDismiss(dialog);
		}
	}

	@Override
	public Node getNode()
	{
		return _node;
	}

	public void setNode(Node node)
	{
		_node = node;
	}

	@Override
	public BaseFragment fragmentForPath(String path)
	{
		return _node.getFragment().fragmentForPath(path);
	}

	public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener)
	{
		_onDismissListener = onDismissListener;
	}
}
