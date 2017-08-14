package mediamonks.com.rnnativenavigation.data;

import java.io.Serializable;

import mediamonks.com.rnnativenavigation.factory.BaseFragment;

/**
 * Created by erik on 09/08/2017.
 * RNNativeNavigation 2017
 */

public interface Node extends Serializable
{
	BaseFragment getFragment();
	String getTitle();
}
