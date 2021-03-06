package com.mediamonks.rnnativenavigation.factory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.mediamonks.rnnativenavigation.data.Node;
import com.mediamonks.rnnativenavigation.data.TabNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erik on 10/08/2017.
 * RNNativeNavigation 2017
 */

public class TabFragment extends BaseFragment<TabNode> implements BottomNavigationView.OnNavigationItemSelectedListener, Navigatable {
	private ViewPager _viewPager;
	private TabPagerAdapter _adapter;
	private ViewPager.SimpleOnPageChangeListener _onPageChangeListener;
	private BottomNavigationView _bottomNavigationView;
	private List<Integer> _items;
	private int _pagerId;

	private static class TabPagerAdapter extends FragmentPagerAdapter {
		private List<Node> _items;
		private List<BaseFragment> _fragments;

		TabPagerAdapter(FragmentManager fm, List<Node> items) {
			super(fm);

			this._items = items;
			this._fragments = new ArrayList<>();
			int leni = items.size();
			for (int i = 0; i < leni; ++i) {
				_fragments.add(null);
			}
		}

		@Override
		public BaseFragment getItem(int position) {
			BaseFragment fragment = _fragments.get(position);
			if (fragment == null) {
				fragment = _items.get(position).generateFragment();
				_fragments.remove(position);
				_fragments.add(position, fragment);
			}
			return fragment;
		}
		
		@Override
		public int getCount() {
			return _items.size();
		}

		List<BaseFragment> getFragments() {
			return _fragments;
		}
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// I'm calling generateViewId() twice, calling it once doesn't work on first load. My assumption is the initial id is later hijacked by ReactNative, making it impossible to add fragments
		View.generateViewId();

		_adapter = new TabPagerAdapter(getChildFragmentManager(), getNode().getTabs());
		_pagerId = View.generateViewId();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		LinearLayout linearLayout = new LinearLayout(getContext());
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		_viewPager = new ViewPager(getContext());
		_viewPager.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
		_viewPager.setId(_pagerId);
		linearLayout.addView(_viewPager);

		_bottomNavigationView = new BottomNavigationView(getContext());
		_bottomNavigationView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		_items = new ArrayList<>();
		for (Node node : getNode().getTabs()) {
			int index = getNode().getTabs().indexOf(node);
			int itemId = View.generateViewId();
			_bottomNavigationView.getMenu().add(0, itemId, index, "todo");
			_items.add(itemId);
		}

		linearLayout.addView(_bottomNavigationView);

		return linearLayout;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		_viewPager.setAdapter(_adapter);
		_viewPager.setCurrentItem(getNode().getSelectedTab());

		_onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				getNode().setSelectedTab(position);
				int itemId = _bottomNavigationView.getMenu().getItem(position).getItemId();
				_bottomNavigationView.setSelectedItemId(itemId);
			}
		};
		_viewPager.addOnPageChangeListener(_onPageChangeListener);

		_bottomNavigationView.setOnNavigationItemSelectedListener(this);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		_viewPager.removeOnPageChangeListener(_onPageChangeListener);
		_onPageChangeListener = null;

		_bottomNavigationView.setOnNavigationItemSelectedListener(null);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		_adapter = null;
	}

	@Override public void callMethodWithName(String name, ReadableMap arguments, RNNNFragment rootFragment, Callback callback) {
		switch (name) {
			case TabNode.OPEN_TAB: {
				this.handleOpenTabCall(arguments);
				break;
			}
		}
	}

	private void handleOpenTabCall(final ReadableMap arguments) {
		final int index = arguments.getInt("index");
		getNode().setSelectedTab(index);

		getActivity().runOnUiThread(new Runnable() {
			@Override public void run() {
				_viewPager.setCurrentItem(index);
			}
		});
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		int index = _items.indexOf(item.getItemId());
		_viewPager.setCurrentItem(index, true);
		return true;
	}

	@Override
	public boolean onBackPressed() {
		BaseFragment fragment = _adapter.getItem(_viewPager.getCurrentItem());
		return fragment.onBackPressed();
	}

	@Override
	public BaseFragment fragmentForPath(String path) {
		if (path.equals(getNode().getScreenID())) {
			return this;
		}
		if (path.indexOf(getNode().getScreenID()) == 0) {
			BaseFragment checkFragment;
			BaseFragment foundFragment = null;

			int i = 0;
			do {
				if (i < _adapter.getCount()) {
					checkFragment = _adapter.getItem(i++);

					if (path.indexOf(checkFragment.getNode().getScreenID()) == 0) {
						foundFragment = checkFragment;
					}
				} else {
					checkFragment = null;
				}
			}
			while (checkFragment != null);

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
		int leni = _adapter.getFragments().size();
		for (int i = 0; i < leni; ++i) {
			BaseFragment fragment = _adapter.getFragments().get(i);
			if (fragment != null) {
				fragment.invalidate();
			}
		}
	}

	@Override
	public SingleFragment getCurrentFragment() {
		BaseFragment currentFragment = _adapter.getItem(_viewPager.getCurrentItem());
		if (currentFragment instanceof SingleFragment) {
			return (SingleFragment) currentFragment;
		}
		return currentFragment.getCurrentFragment();
	}
}