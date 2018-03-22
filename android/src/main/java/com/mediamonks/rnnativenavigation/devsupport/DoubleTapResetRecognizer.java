package com.mediamonks.rnnativenavigation.devsupport;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by erik on 16/01/2018.
 * example 2018
 */

class DoubleTapResetRecognizer {
	private boolean _doReset = false;
	private static final long DOUBLE_TAP_DELAY = 200;

	boolean didDoubleTapE(int keyCode, View view) {
		if (keyCode == KeyEvent.KEYCODE_N && !(view instanceof EditText)) {
			if (_doReset) {
				_doReset = false;
				return true;
			} else {
				_doReset = true;
				new Handler().postDelayed(new Runnable() {
					@Override public void run() {
						_doReset = false;
					}
				}, DOUBLE_TAP_DELAY);
			}
		}
		return false;
	}
}
