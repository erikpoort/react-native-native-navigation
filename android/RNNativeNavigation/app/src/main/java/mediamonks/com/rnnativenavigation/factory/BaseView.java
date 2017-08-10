package mediamonks.com.rnnativenavigation.factory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by erik on 10/08/2017.
 * RNNativeNavigation 2017
 */

abstract class BaseView extends AppCompatActivity
{
	private Toolbar _toolbar;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		_toolbar = new Toolbar(this);
		TypedValue tv = new TypedValue();
		if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
		{
			_toolbar.setLayoutParams(new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics())));
		}
		_toolbar.setVisibility(View.VISIBLE);
		this.setSupportActionBar(_toolbar);

		assert getSupportActionBar() != null;
		getSupportActionBar().setDisplayHomeAsUpEnabled(!isTaskRoot());
	}

	public Toolbar getToolbar()
	{
		return _toolbar;
	}
}
