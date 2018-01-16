package com.mediamonks.rnnativenavigation.react;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.react.ReactRootView;

/**
 * Created by erik on 16/01/2018.
 * example 2018
 */

public class RNRootView extends ReactRootView {
    private boolean _invalidated;

    public RNRootView(Context context) {
        super(context);
    }

    public RNRootView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RNRootView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void invalidate() {
        _invalidated = true;
        unmountReactApplication();
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (_invalidated) {
            setMeasuredDimension(
                    MeasureSpec.getSize(widthMeasureSpec),
                    MeasureSpec.getSize(heightMeasureSpec));
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
