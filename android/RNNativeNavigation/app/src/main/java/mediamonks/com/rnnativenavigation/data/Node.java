package mediamonks.com.rnnativenavigation.data;

import android.content.Context;
import android.support.v4.app.TaskStackBuilder;

import java.io.Serializable;

/**
 * Created by erik on 09/08/2017.
 * RNNativeNavigation 2017
 */

public interface Node extends Serializable
{
	TaskStackBuilder generate(Context context, TaskStackBuilder stackBuilder);
}
