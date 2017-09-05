package com.mediamonks.rnnativenavigation.data;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;

/**
 * Created by erik on 05/09/2017.
 * example 2017
 */

public class BaseNode
{
	private static final String SCREEN_ID = "screenID";
	private String _screenID;

	public void setData(ReadableMap map)
	{
		_screenID = map.getString(SCREEN_ID);
	}

	public WritableMap data()
	{
		WritableNativeMap data = new WritableNativeMap();
		data.putString(SCREEN_ID, _screenID);
		return data;
	}

	public String getScreenID()
	{
		return _screenID;
	}

}
