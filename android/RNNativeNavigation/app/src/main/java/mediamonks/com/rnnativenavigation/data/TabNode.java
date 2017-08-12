package mediamonks.com.rnnativenavigation.data;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import mediamonks.com.rnnativenavigation.factory.BaseFragment;
import mediamonks.com.rnnativenavigation.factory.TabView;

/**
 * Created by erik on 10/08/2017.
 * RNNativeNavigation 2017
 */

public class TabNode implements Node
{
	public static final String TABS_KEY = "TABS_KEY";
	public static final String SELECTED_TAB_KEY = "SELECTED_TAB_KEY";

	private final ArrayList<Node> _tabs;
	private final int _selectedTab;

	public TabNode(ArrayList<Node> tabs, int selectedTab)
	{
		super();

		_tabs = tabs;
		_selectedTab = selectedTab;
	}


	@Override
	public Intent getIntent(Context context)
	{
		Intent intent = new Intent(context, TabView.class);
		intent.putExtra(TABS_KEY, _tabs);
		intent.putExtra(SELECTED_TAB_KEY, _selectedTab);
		return intent;
	}

	@Override
	public Class <? extends BaseFragment> getFragmentClass()
	{
		return TabView.TabFragment.class;
	}
}
