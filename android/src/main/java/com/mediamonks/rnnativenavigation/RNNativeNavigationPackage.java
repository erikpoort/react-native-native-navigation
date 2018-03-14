package com.mediamonks.rnnativenavigation;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.mediamonks.rnnativenavigation.bridge.RNNativeNavigationModule;
import com.mediamonks.rnnativenavigation.data.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by erik on 29/08/2017.
 * example 2017
 */

public class RNNativeNavigationPackage implements ReactPackage {
	private final Map<String, Class<? extends Node>> _externalNodes;

	public RNNativeNavigationPackage() {
		_externalNodes = new HashMap<>();
	}

	public RNNativeNavigationPackage(Map<String, Class<? extends Node>> externalNodes) {
		_externalNodes = externalNodes;
	}

	@Override
	public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
		List<NativeModule> modules = new ArrayList<>();
		modules.add(new RNNativeNavigationModule(reactContext, _externalNodes));
		return modules;
	}

	@Override
	public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
		return Collections.emptyList();
	}
}
