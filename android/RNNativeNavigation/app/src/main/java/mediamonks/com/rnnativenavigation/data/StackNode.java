package mediamonks.com.rnnativenavigation.data;

import java.util.ArrayList;

import mediamonks.com.rnnativenavigation.factory.BaseFragment;
import mediamonks.com.rnnativenavigation.factory.StackFragment;

/**
 * Created by erik on 12/08/2017.
 * RNNativeNavigation 2017
 */

public class StackNode implements Node
{
	private final String _title;
	private final ArrayList<Node> _stack;

	public StackNode(String title, ArrayList<Node> stack)
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

	public ArrayList<Node> getStack()
	{
		return _stack;
	}
}
