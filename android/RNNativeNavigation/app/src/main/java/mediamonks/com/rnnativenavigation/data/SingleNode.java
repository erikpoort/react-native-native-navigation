package mediamonks.com.rnnativenavigation.data;

import mediamonks.com.rnnativenavigation.factory.BaseFragment;
import mediamonks.com.rnnativenavigation.factory.SingleFragment;

/**
 * Created by erik on 09/08/2017.
 * RNNativeNavigation 2017
 */

public class SingleNode implements Node
{
	private final String _title;

	public SingleNode(String title)
	{
		_title = title;
	}

	@Override
	public BaseFragment<SingleNode> getFragment()
	{
		SingleFragment fragment = new SingleFragment();
		fragment.setNode(this);
		return fragment;
	}

	public String getTitle()
	{
		return _title;
	}
}
