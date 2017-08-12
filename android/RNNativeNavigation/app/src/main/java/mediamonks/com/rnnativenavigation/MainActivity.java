package mediamonks.com.rnnativenavigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

import mediamonks.com.rnnativenavigation.data.Node;
import mediamonks.com.rnnativenavigation.data.SingleNode;
import mediamonks.com.rnnativenavigation.data.TabNode;

/**
 * Created by erik on 10/08/2017.
 * RNNativeNavigation 2017
 */

public class MainActivity extends Activity
{
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Node node = new TabNode(new ArrayList<Node>(Arrays.asList(
				new SingleNode(),
				new SingleNode(),
				new SingleNode()
		)), 0);
//		Node node = new SingleNode();

		Intent intent = node.getIntent(this);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		this.startActivity(intent);
	}
}
