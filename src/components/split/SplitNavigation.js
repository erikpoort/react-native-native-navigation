import React from 'react';
import { NativeModules } from 'react-native';
import BaseNavigation from '../BaseNavigation';

const { ReactNativeNativeNavigation } = NativeModules;

export default class SplitNavigation extends BaseNavigation {
	replace = (node, showScreen) => {
		return super.addScreens(`${this.screenID}/${node}`, ReactNativeNativeNavigation.replace, showScreen, null)
	}
}