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
	private static final String TYPE = "type";

	private String _screenID;
	private String _type;

	public void setData(ReadableMap map)
	{
		_screenID = map.getString(SCREEN_ID);
		_type = map.getString(TYPE);
	}

	public WritableMap data()
	{
		WritableNativeMap data = new WritableNativeMap();
		data.putString(SCREEN_ID, _screenID);
		data.putString(TYPE, _type);
		return data;
	}

	public String getScreenID()
	{
		return _screenID;
	}

	public String getRootPath()
	{
		int index = getScreenID().indexOf("/", 1);
		if (index > -1 && index < getScreenID().length())
		{
			return getScreenID().substring(0, index);
		}
		else if (index < 0)
		{
			return getScreenID();
		}
		else
		{
			return "";
		}
	}
}
