package mediamonks.com.rnnativenavigation.data;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.io.Serializable;

import mediamonks.com.rnnativenavigation.factory.BaseFragment;

/**
 * Created by erik on 09/08/2017.
 * RNNativeNavigation 2017
 */

public interface Node extends Serializable
{
	Intent getIntent(Context context);
	Class <? extends BaseFragment> getFragmentClass();
}
