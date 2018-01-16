package com.example;

import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactFragmentActivity;
import com.mediamonks.rnnativenavigation.devsupport.RNActivityDelegate;

public class MainActivity extends ReactFragmentActivity
{
    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "example";
    }

    @Override protected ReactActivityDelegate createReactActivityDelegate() {
        return new RNActivityDelegate(this, getMainComponentName());
    }
}
