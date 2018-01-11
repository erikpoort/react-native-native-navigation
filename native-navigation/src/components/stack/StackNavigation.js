import React from 'react';
import { NativeModules } from 'react-native';

const { ReactNativeNativeNavigation } = NativeModules;

import BaseNavigation from '../BaseNavigation';

export default class StackNavigation extends BaseNavigation{
	handleBackButton = (callback) => {
		ReactNativeNativeNavigation.handleBackButton(callback);
	};

	push = (showScreen) => {
		return super.registerScreens(this.screenID, ReactNativeNativeNavigation.push, showScreen);
	}
}
