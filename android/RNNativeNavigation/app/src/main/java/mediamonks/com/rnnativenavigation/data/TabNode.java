package mediamonks.com.rnnativenavigation.data;

import java.util.ArrayList;

import mediamonks.com.rnnativenavigation.factory.BaseFragment;
import mediamonks.com.rnnativenavigation.factory.TabFragment;

/**
 * Created by erik on 10/08/2017.
 * RNNativeNavigation 2017
 */

public class TabNode implements Node
{
	private final ArrayList<TitleNode> _tabs;
	private final int _selectedTab;

	public TabNode(ArrayList<TitleNode> tabs, int selectedTab)
	{
		super();

		_tabs = tabs;
		_selectedTab = selectedTab;
	}

	@Override
	public BaseFragment getFragment()
	{
		TabFragment fragment = new TabFragment();
		fragment.setNode(this);
		return fragment;
	}

	public ArrayList<TitleNode> getTabs()
	{
		return _tabs;
	}

	public int getSelectedTab()
	{
		return _selectedTab;
	}
}
