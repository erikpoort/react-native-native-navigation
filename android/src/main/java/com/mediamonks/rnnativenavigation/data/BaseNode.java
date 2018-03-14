package com.mediamonks.rnnativenavigation.data;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;

/**
 * Created by erik on 05/09/2017.
 * example 2017
 */

public abstract class BaseNode implements Node {
	private static final String SCREEN_ID = "screenID";
	private static final String TYPE = "type";

	private ReactInstanceManager _instanceManager;

	private String _screenID;
	private String _type;

	private boolean _shown;

	public void setInstanceManager(ReactInstanceManager instanceManager) {
		_instanceManager = instanceManager;
	}

	public ReactInstanceManager getInstanceManager() {
		return _instanceManager;
	}

	public void setData(ReadableMap map) {
		_screenID = map.getString(SCREEN_ID);
		_type = map.getString(TYPE);
	}

	public WritableMap getData() {
		WritableNativeMap data = new WritableNativeMap();
		data.putString(SCREEN_ID, _screenID);
		data.putString(TYPE, _type);
		return data;
	}

	public void setShown(boolean shown) {
		_shown = shown;
	}

	public boolean isShown() {
		return _shown;
	}

	public String getScreenID() {
		return _screenID;
	}

	public String getRootPath() {
		int index = getScreenID().indexOf("/", 1);
		if (index > -1 && index < getScreenID().length()) {
			return getScreenID().substring(0, index);
		} else if (index < 0) {
			return getScreenID();
		} else {
			return "";
		}
	}
}
