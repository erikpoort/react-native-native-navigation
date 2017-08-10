package mediamonks.com.rnnativenavigation.factory;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Random;

/**
 * Created by erik on 09/08/2017.
 * RNNativeNavigation 2017
 */

public class SingleView extends BaseView
{
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Random rand = new Random();
		int r = rand.nextInt(255);
		int g = rand.nextInt(255);
		int b = rand.nextInt(255);
		int randomColor = Color.rgb(r, g, b);

		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		linearLayout.addView(getToolbar());

		linearLayout.setBackgroundColor(randomColor);
		setContentView(linearLayout);
	}

	@Override
	public void onStart()
	{
		super.onStart();

		Log.i("MMM", "onStart");
	}

	@Override
	public void onPause()
	{
		super.onPause();

		Log.i("MMM", "onPause");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				this.onBackPressed();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
