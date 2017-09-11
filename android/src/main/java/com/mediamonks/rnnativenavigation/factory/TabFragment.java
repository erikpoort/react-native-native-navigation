package com.mediamonks.rnnativenavigation.factory;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mediamonks.rnnativenavigation.data.Node;
import com.mediamonks.rnnativenavigation.data.TabNode;

import java.util.List;

/**
 * Created by erik on 10/08/2017.
 * RNNativeNavigation 2017
 */

public class TabFragment extends BaseFragment<TabNode> implements BottomNavigationView.OnNavigationItemSelectedListener
{
	private ViewPager _viewPager;
	private TabPagerAdapter _adapter;

	private static class TabPagerAdapter extends FragmentPagerAdapter
	{
		private List<Node> _items;

		private SparseArray<BaseFragment> _fragments;

		TabPagerAdapter(FragmentManager fm, List<Node> items)
		{
			super(fm);

			this._items = items;
			this._fragments = new SparseArray<>(this._items.size());
		}

		@Override
		public BaseFragment getItem(int position)
		{
			try
			{
				if (_fragments.get(position) == null)
				{
					_fragments.put(position, _items.get(position).getFragment());
				}
				return _fragments.get(position);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			return null;
		}

		@Override
		public int getCount()
		{
			return _items.size();
		}
		SparseArray<BaseFragment> getFragments()
		{
			return _fragments;
		}

	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		LinearLayout linearLayout = new LinearLayout(getContext());
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		_viewPager = new ViewPager(getContext());
		_viewPager.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
		// I'm calling generateViewId() twice, calling it once doesn't work on first load. My assumption is the initial id is later hijacked by ReactNative, making it impossible to add fragments
		View.generateViewId();
		_viewPager.setId(View.generateViewId());
		_viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
				super.onPageSelected(position);
				getNode().setSelectedTab(position);
			}
		});
		linearLayout.addView(_viewPager);

		BottomNavigationView bottomNavigationView = new BottomNavigationView(getContext());
		bottomNavigationView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		int[][] states = new int[][]{
				new int[]{android.R.attr.state_enabled}, // enabled
				new int[]{android.R.attr.state_checkable}, // disabled
		};
		int[] colors = new int[]{
				Color.WHITE,
				Color.GRAY,
		};
		for (Node node : getNode().getTabs())
		{
			String title = node.getTitle();
			int i = getNode().getTabs().indexOf(node);
			bottomNavigationView.getMenu().add(0, i, i, title);
		}
		bottomNavigationView.setItemTextColor(new ColorStateList(states, colors));
		bottomNavigationView.setOnNavigationItemSelectedListener(this);
		linearLayout.addView(bottomNavigationView);

		return linearLayout;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		_adapter = new TabPagerAdapter(getActivity().getSupportFragmentManager(), getNode().getTabs());
		_viewPager.setAdapter(_adapter);
		_viewPager.setCurrentItem(getNode().getSelectedTab());
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item)
	{
		_viewPager.setCurrentItem(item.getItemId(), true);
		return true;
	}

	@Override
	public boolean onBackPressed()
	{
		TabPagerAdapter adapter = (TabPagerAdapter) _viewPager.getAdapter();
		BaseFragment fragment = adapter.getFragments().get(_viewPager.getCurrentItem());
		return fragment.onBackPressed();
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
				if (i < _adapter.getCount())
				{
					checkFragment = _adapter.getItem(i++);

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
		BaseFragment currentFragment = _adapter.getItem(_viewPager.getCurrentItem());
		if (currentFragment instanceof SingleFragment) {
			return (SingleFragment) currentFragment;
		}
		return currentFragment.getCurrentFragment();
	}
}