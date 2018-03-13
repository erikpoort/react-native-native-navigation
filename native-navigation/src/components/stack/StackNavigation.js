import React from 'react';
import { NativeModules } from 'react-native';
import BaseNavigation from '../BaseNavigation';

const { ReactNativeNativeNavigation } = NativeModules;

export default class StackNavigation extends BaseNavigation {
	handleBackButton = (callback) => {
		ReactNativeNativeNavigation.handleBackButton(callback);
	};

	push = (showScreen, reset = false) => {
		return super.addScreens(this.screenID, ReactNativeNativeNavigation.push, showScreen, { reset });
	};

	pop = () => {
		return ReactNativeNativeNavigation.callMethodOnNode(this.navigatorID, ReactNativeNativeNavigation.pop, null, () => {});
	};
}
