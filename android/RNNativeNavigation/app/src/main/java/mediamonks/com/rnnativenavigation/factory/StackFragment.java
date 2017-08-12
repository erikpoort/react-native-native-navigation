package mediamonks.com.rnnativenavigation.factory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import mediamonks.com.rnnativenavigation.data.Node;
import mediamonks.com.rnnativenavigation.data.StackNode;

/**
 * Created by erik on 12/08/2017.
 * RNNativeNavigation 2017
 */

public class StackFragment extends BaseFragment<StackNode>
{
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		LinearLayout linearLayout = new LinearLayout(getContext());
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		Toolbar toolbar = new Toolbar(getContext());
		TypedValue typedValue = new TypedValue();
		if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true))
		{
			toolbar.setLayoutParams(new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics())));
		}
		toolbar.setTitle(getNode().getTitle());
		linearLayout.addView(toolbar);

		FrameLayout holder = new FrameLayout(getContext());
		holder.setId(View.generateViewId());
		linearLayout.addView(holder, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1));

		boolean firstNode = true;
		for (Node node : getNode().getStack())
		{
			FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
			transaction.add(holder.getId(), node.getFragment());
			if (!firstNode) {
				transaction.addToBackStack(null);
			}
			firstNode = false;
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			transaction.commit();
		}

		getBaseActivity().setSupportActionBar(toolbar);
		final ActionBar actionBar = getBaseActivity().getSupportActionBar();
		assert actionBar != null;
		actionBar.setDisplayHomeAsUpEnabled(getNode().getStack().size() > 1);

		holder.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener()
		{
			void handleActionBar(View parent)
			{
				ViewGroup viewGroup = (ViewGroup) parent;
				int index = viewGroup.getChildCount() - 1;

				if (index >= 0)
				{
					actionBar.setTitle(getNode().getStack().get(index).getTitle());
					actionBar.setDisplayHomeAsUpEnabled(index > 0);
				}
			}

			@Override
			public void onChildViewAdded(View parent, View child)
			{
				handleActionBar(parent);
			}

			@Override
			public void onChildViewRemoved(View parent, View child)
			{
				handleActionBar(parent);
			}
		});

		return linearLayout;
	}
}
