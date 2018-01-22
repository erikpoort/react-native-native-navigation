import React from 'react';
import { NativeModules } from 'react-native';
import BaseNavigation from '../BaseNavigation';

const { ReactNativeNativeNavigation } = NativeModules;

export default class SingleNavigation extends BaseNavigation {
	showModal = (showScreen) => {
		const newPath = `${this.screenID}/modal`;
		return super.registerScreens(newPath, "showModal", showScreen, null);
	}
}