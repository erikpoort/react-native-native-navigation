import React from 'react';
import { NativeModules } from 'react-native';
import BaseNavigation from '../BaseNavigation';

const { ReactNativeNativeNavigation } = NativeModules;

export default class StackNavigation extends BaseNavigation {
	handleBackButton = (callback) => {
		ReactNativeNativeNavigation.handleBackButton(callback);
	};

	push = (showScreen) => {
		return super.addScreens(this.screenID, ReactNativeNativeNavigation.push, showScreen, null);
	};

	pop = () => {
		return ReactNativeNativeNavigation.callMethodOnNode(this.navigatorID, ReactNativeNativeNavigation.pop, null, () => {});
	};
}
