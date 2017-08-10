package mediamonks.com.rnnativenavigation.data;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;

import mediamonks.com.rnnativenavigation.factory.SingleView;

/**
 * Created by erik on 09/08/2017.
 * RNNativeNavigation 2017
 */

public class SingleNode implements Node
{
	@Override
	public TaskStackBuilder generate(Context context, TaskStackBuilder stackBuilder)
	{
		return stackBuilder.addNextIntent(new Intent(context, SingleView.class));
	}
}
