package com.mediamonks.rnnativenavigation.devsupport;

import android.os.Bundle;
import android.view.KeyEvent;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactFragmentActivity;
import com.mediamonks.rnnativenavigation.bridge.RNNativeNavigationModule;

import javax.annotation.Nullable;

/**
 * Created by erik on 16/01/2018.
 * example 2018
 */

public class RNActivityDelegate extends ReactActivityDelegate {
    private final ReactFragmentActivity _activity;

    private @Nullable DoubleTapResetRecognizer _doubleTapResetRecognizer;

    public RNActivityDelegate(ReactFragmentActivity fragmentActivity, @Nullable String mainComponentName) {
        super(fragmentActivity, mainComponentName);
        _activity = fragmentActivity;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _doubleTapResetRecognizer = new DoubleTapResetRecognizer();
    }

    @Override public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (getReactNativeHost().hasInstance() && getReactNativeHost().getUseDeveloperSupport()) {
            boolean didDoubleTapE = Assertions.assertNotNull(_doubleTapResetRecognizer)
                    .didDoubleTapE(keyCode, _activity.getCurrentFocus());
            if (didDoubleTapE) {
                getReactInstanceManager().getCurrentReactContext().getNativeModule(RNNativeNavigationModule.class).resetNavigation();
            }
        }

        return super.onKeyUp(keyCode, event);
    }
}
