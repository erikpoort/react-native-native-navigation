import React from 'react';
import { NativeModules } from 'react-native';
import BaseNavigation from '../BaseNavigation';

const { ReactNativeNativeNavigation } = NativeModules;

export default class TabNavigation extends BaseNavigation {
	openTab = (index) => {
		return ReactNativeNativeNavigation.callMethodOnNode(this.navigatorID, ReactNativeNativeNavigation.openTab, { index }, () => {});
	};
	removeTab = (index, animated) => {
		return ReactNativeNativeNavigation.callMethodOnNode(this.navigatorID, ReactNativeNativeNavigation.removeTab, { index, animated }, () => {});
	};
}