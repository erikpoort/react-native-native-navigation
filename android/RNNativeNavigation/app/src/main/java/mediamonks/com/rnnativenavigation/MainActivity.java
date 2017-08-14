package mediamonks.com.rnnativenavigation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;

import mediamonks.com.rnnativenavigation.data.Node;
import mediamonks.com.rnnativenavigation.data.SingleNode;
import mediamonks.com.rnnativenavigation.data.StackNode;
import mediamonks.com.rnnativenavigation.data.TabNode;
import mediamonks.com.rnnativenavigation.factory.BaseFragment;

/**
 * Created by erik on 10/08/2017.
 * RNNativeNavigation 2017
 */

public class MainActivity extends AppCompatActivity
{
	private BaseFragment _fragment;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		TabNode node = new TabNode(new ArrayList<>(Arrays.asList(
				new SingleNode("A"),
				new SingleNode("B"),
				new SingleNode("C"),
				new StackNode("D", new ArrayList<>(Arrays.asList(
						new TabNode(new ArrayList<Node>(Arrays.asList(
								new SingleNode("A"),
								new SingleNode("B"),
								new SingleNode("C")
						)), 1),
						new SingleNode("3"),
						new SingleNode("2"),
						new SingleNode("1")
				)))
		)), 1);

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		_fragment = node.getFragment();
		transaction.add(android.R.id.content, _fragment);
		transaction.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed()
	{
		if (this._fragment.onBackPressed())
		{
			return;
		}
		finish();
	}
}
