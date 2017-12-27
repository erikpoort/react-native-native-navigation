import React from 'react';
import { NativeModules } from 'react-native';

const { ReactNativeNativeNavigation } = NativeModules;
import { Navigation } from './../Navigation';
import SingleView from './../single/SingleView';

import BaseNavigation from '../BaseNavigation';

export default class StackNavigation extends BaseNavigation{
	screenID;

	constructor(screenID) {
		super()
		this.screenID = screenID;
	}

	handleBackButton = (callback) => {
		ReactNativeNativeNavigation.handleBackButton(callback);
	}

	push = (showScreen) => {
		return super.registerScreens(this, Navigation, showScreen);
		
	}
}
