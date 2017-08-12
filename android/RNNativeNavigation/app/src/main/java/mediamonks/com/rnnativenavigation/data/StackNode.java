package mediamonks.com.rnnativenavigation.data;

import java.util.ArrayList;

import mediamonks.com.rnnativenavigation.factory.BaseFragment;
import mediamonks.com.rnnativenavigation.factory.StackFragment;

/**
 * Created by erik on 12/08/2017.
 * RNNativeNavigation 2017
 */

public class StackNode implements TitleNode
{
	private final String _title;
	private final ArrayList<TitleNode> _stack;

	public StackNode(String title, ArrayList<TitleNode> stack)
	{
		super();

		_title = title;
		_stack = stack;
	}

	@Override
	public BaseFragment<StackNode> getFragment()
	{
		StackFragment fragment = new StackFragment();
		fragment.setNode(this);
		return fragment;
	}

	public String getTitle()
	{
		return _title;
	}

	public ArrayList<TitleNode> getStack()
	{
		return _stack;
	}
}
