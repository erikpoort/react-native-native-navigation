package mediamonks.com.rnnativenavigation.factory;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import java.util.Random;

/**
 * Created by erik on 09/08/2017.
 * RNNativeNavigation 2017
 */

public class SingleView extends BaseActivity
{
	public static class SingleFragment extends BaseFragment
	{
		@Nullable
		@Override
		public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
		{
			super.onCreateView(inflater, container, savedInstanceState);

			Random rand = new Random();
			int r = rand.nextInt(255);
			int g = rand.nextInt(255);
			int b = rand.nextInt(255);
			int randomColor = Color.rgb(r, g, b);

			getActivity().setTitle(randomColor + "");

			FrameLayout frameLayout = new FrameLayout(getContext());
			frameLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			frameLayout.setBackgroundColor(randomColor);
			frameLayout.addView(getToolbar());
			return frameLayout;
		}
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		FrameLayout frameLayout = new FrameLayout(this);
		setContentView(frameLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		Fragment fragment = getFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(android.R.id.content, fragment);
		transaction.commit();
	}

	@Override
	public BaseFragment getFragment()
	{
		return new SingleFragment();
	}
}
