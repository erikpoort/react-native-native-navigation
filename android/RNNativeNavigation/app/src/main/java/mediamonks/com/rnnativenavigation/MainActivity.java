package mediamonks.com.rnnativenavigation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Arrays;

import mediamonks.com.rnnativenavigation.data.Node;
import mediamonks.com.rnnativenavigation.data.SingleNode;
import mediamonks.com.rnnativenavigation.data.StackNode;
import mediamonks.com.rnnativenavigation.data.TabNode;
import mediamonks.com.rnnativenavigation.data.TitleNode;
import mediamonks.com.rnnativenavigation.factory.BaseActivity;
import mediamonks.com.rnnativenavigation.factory.BaseFragment;

/**
 * Created by erik on 10/08/2017.
 * RNNativeNavigation 2017
 */

public class MainActivity extends BaseActivity
{
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		StackNode stackNode = new StackNode("c", new ArrayList<TitleNode>(Arrays.asList(
				new SingleNode("3"),
				new SingleNode("2"),
				new SingleNode("1")
		)));
		TabNode tabNode = new TabNode(new ArrayList<>(Arrays.asList(
				new SingleNode("a"),
				new SingleNode("b"),
				stackNode
		)), 1);
		Node node = new SingleNode("Title");

		try
		{
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			BaseFragment fragment = tabNode.getFragment();
			transaction.add(android.R.id.content, fragment);
			transaction.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
