package mediamonks.com.rnnativenavigation.factory;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by erik on 10/08/2017.
 * RNNativeNavigation 2017
 */

public abstract class BaseFragment extends Fragment
{
	private Toolbar _toolbar;

	public Toolbar getToolbar()
	{
		if (_toolbar == null) {
			_toolbar = new Toolbar(getBaseActivity());
			TypedValue tv = new TypedValue();
			if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
			{
				_toolbar.setLayoutParams(new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics())));
			}
			_toolbar.setVisibility(View.VISIBLE);
			getBaseActivity().setSupportActionBar(_toolbar);

			assert getBaseActivity().getSupportActionBar() != null;
			getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(!getBaseActivity().isTaskRoot());
		}

		return _toolbar;
	}

	private BaseActivity getBaseActivity()
	{
		return (BaseActivity) getActivity();
	}


}
