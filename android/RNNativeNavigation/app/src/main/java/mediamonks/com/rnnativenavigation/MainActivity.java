package mediamonks.com.rnnativenavigation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;

import mediamonks.com.rnnativenavigation.data.SingleNode;
import mediamonks.com.rnnativenavigation.data.StackNode;
import mediamonks.com.rnnativenavigation.data.TabNode;
import mediamonks.com.rnnativenavigation.data.TitleNode;
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

		StackNode stackNode = new StackNode("c", new ArrayList<TitleNode>(Arrays.asList(
				new SingleNode("3"),
				new SingleNode("2"),
				new SingleNode("1")
		)));
		TabNode tabNode = new TabNode(new ArrayList<TitleNode>(Arrays.asList(
				new StackNode("c", new ArrayList<TitleNode>(Arrays.asList(
						new SingleNode("3"),
						new SingleNode("2"),
						new SingleNode("1")
				))),
				new StackNode("b", new ArrayList<TitleNode>(Arrays.asList(
						new SingleNode("3"),
						new SingleNode("2"),
						new SingleNode("1")
				))),
				stackNode
		)), 1);

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		_fragment = tabNode.getFragment();
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
		if (this._fragment.onBackPressed()) {
			return;
		}
		finish();
	}
}
