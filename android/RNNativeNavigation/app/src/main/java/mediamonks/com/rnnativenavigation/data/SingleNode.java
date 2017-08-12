package mediamonks.com.rnnativenavigation.data;

import android.content.Context;
import android.content.Intent;

import mediamonks.com.rnnativenavigation.factory.BaseFragment;
import mediamonks.com.rnnativenavigation.factory.SingleView;

/**
 * Created by erik on 09/08/2017.
 * RNNativeNavigation 2017
 */

public class SingleNode implements Node
{
	@Override
	public Intent getIntent(Context context)
	{
		return new Intent(context, SingleView.class);
	}

	@Override
	public Class<? extends BaseFragment> getFragmentClass()
	{
		return SingleView.SingleFragment.class;
	}
}
