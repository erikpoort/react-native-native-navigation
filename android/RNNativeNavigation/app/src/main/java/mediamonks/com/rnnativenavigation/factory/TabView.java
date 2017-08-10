package mediamonks.com.rnnativenavigation.factory;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by erik on 10/08/2017.
 * RNNativeNavigation 2017
 */

public class TabView extends BaseView
{
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		linearLayout.addView(getToolbar());

		FrameLayout frameLayout = new FrameLayout(this);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
		frameLayout.setLayoutParams(layoutParams);
		linearLayout.addView(frameLayout);

		BottomNavigationView bottomNavigationView = new BottomNavigationView(this);
		bottomNavigationView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		int[][] states = new int[][]{
				new int[]{android.R.attr.state_enabled}, // enabled
				new int[]{-android.R.attr.state_enabled}, // disabled
				new int[]{-android.R.attr.state_checked}, // unchecked
				new int[]{android.R.attr.state_pressed}  // pressed
		};

		int[] colors = new int[]{
				Color.BLACK,
				Color.RED,
				Color.GREEN,
				Color.BLUE
		};
		bottomNavigationView.setItemTextColor(new ColorStateList(states, colors));
		bottomNavigationView.getMenu().add(0, 0, 0, "A");
		bottomNavigationView.getMenu().add(0, 1, 1, "B");
		bottomNavigationView.getMenu().add(0, 2, 2, "C");
		bottomNavigationView.getMenu().add(0, 3, 3, "D");
		linearLayout.addView(bottomNavigationView);

		setContentView(linearLayout);
	}
}
