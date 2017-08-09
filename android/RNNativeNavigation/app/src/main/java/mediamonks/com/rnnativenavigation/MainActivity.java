package mediamonks.com.rnnativenavigation;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import mediamonks.com.rnnativenavigation.data.Node;
import mediamonks.com.rnnativenavigation.data.SingleNode;
import mediamonks.com.rnnativenavigation.factory.SingleView;

public class MainActivity extends AppCompatActivity {

/**
 * type: Drawer
 * left: {}
 * center: {}
 * right: {}
 */

/**
 * type: Tab
 * tabs: [{}]
 */

/**
 * type: Stack
 * stack: [{}]
 */

/**
 * type: Split
 * orientation: vertical|horizontal
 * views: [{}]
 */

/**
 * type: Modal
 * view: {}
 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		FrameLayout frameLayout = new FrameLayout(this);
		setContentView(frameLayout, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		Fragment fragment = new SingleView();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(android.R.id.content, fragment);
		transaction.commit();
    }
}
