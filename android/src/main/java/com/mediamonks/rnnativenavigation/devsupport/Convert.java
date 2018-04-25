package com.mediamonks.rnnativenavigation.devsupport;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.StrictMode;

import com.facebook.react.bridge.ReadableMap;

import java.net.URI;
import java.net.URL;

public class Convert {
	public static BitmapDrawable drawable(ReadableMap readableMap, Resources resources) {
		URI uri = URI.create(readableMap.getString("uri"));
		String scheme = uri.getScheme();
		if (scheme.equals("http")) {
			return loadDevelop(uri, resources);
		} else if (scheme.equals("file")) {
			return loadLocal(uri, resources);
		}
		return null;
	}

	private static BitmapDrawable loadDevelop(URI uri, Resources resources) {
		try {
			StrictMode.ThreadPolicy threadPolicy = StrictMode.getThreadPolicy();
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
			URL url = new URL(uri.toString());
			Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
			BitmapDrawable drawable = new BitmapDrawable(resources, bitmap);
			StrictMode.setThreadPolicy(threadPolicy);
			return drawable;
		} catch (Exception e) {
			return null;
		}
	}

	private static BitmapDrawable loadLocal(URI uri, Resources resources) {
		Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
		return new BitmapDrawable(resources, bitmap);
	}
}
