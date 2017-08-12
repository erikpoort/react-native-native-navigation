package mediamonks.com.rnnativenavigation.factory;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

import mediamonks.com.rnnativenavigation.data.Node;
import mediamonks.com.rnnativenavigation.data.TabNode;

/**
 * Created by erik on 10/08/2017.
 * RNNativeNavigation 2017
 */

public class TabView extends BaseActivity
{
	private static class TabPagerAdapter extends FragmentStatePagerAdapter
	{
		private ArrayList<Node> _items;

		TabPagerAdapter(FragmentManager fm, ArrayList<Node> items)
		{
			super(fm);

			this._items = items;
		}

		@Override
		public Fragment getItem(int position)
		{
			try
			{
				return (Fragment) _items.get(position).getFragmentClass().newInstance();
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
	}

	public static class TabFragment extends BaseFragment implements BottomNavigationView.OnNavigationItemSelectedListener
	{
		private ArrayList<Node> _items;
		private int _selectedTab;
		private SparseArray<BaseFragment> _fragments;
		private ViewPager _viewPager;

		@Nullable
		@Override
		public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
		{
			_fragments = new SparseArray<>();

			LinearLayout linearLayout = new LinearLayout(getContext());
			linearLayout.setOrientation(LinearLayout.VERTICAL);
			linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

			linearLayout.addView(getToolbar());

			_viewPager = new ViewPager(getActivity());
			_viewPager.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
			_viewPager.setId(View.generateViewId());
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
			bottomNavigationView.setItemTextColor(new ColorStateList(states, colors));
			bottomNavigationView.setOnNavigationItemSelectedListener(this);
			linearLayout.addView(bottomNavigationView);

			_items = (ArrayList<Node>) getActivity().getIntent().getSerializableExtra(TabNode.TABS_KEY);
			_selectedTab = getActivity().getIntent().getIntExtra(TabNode.SELECTED_TAB_KEY, 0);

			int leni = _items.size();

			FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
			PagerAdapter adapter = new TabPagerAdapter(getActivity().getSupportFragmentManager(), _items);

			for (int i = 0; i < leni; ++i)
			{
				Node node = _items.get(i);
				try
				{
					Intent intent = node.getIntent(getContext());
					Class<BaseActivity> nodeClass = (Class<BaseActivity>) Class.forName(intent.getComponent().getClassName());
					BaseActivity baseView = nodeClass.newInstance();
					BaseFragment fragment = baseView.getFragment();
					_fragments.put(i, fragment);

					bottomNavigationView.getMenu().add(0, i, i, baseView.getTitle());
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			transaction.commit();

			_viewPager.setAdapter(adapter);
			bottomNavigationView.setSelectedItemId(_selectedTab);

			return linearLayout;
		}

		@Override
		public boolean onNavigationItemSelected(@NonNull MenuItem item)
		{
			_viewPager.setCurrentItem(item.getItemId(), true);
			return true;
		}
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		FrameLayout frameLayout = new FrameLayout(this);
		setContentView(frameLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		Fragment fragment = getFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(android.R.id.content, fragment);
		transaction.commit();
	}

	@Override
	public BaseFragment getFragment()
	{
		return new TabFragment();
	}
}
