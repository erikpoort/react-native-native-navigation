import React from 'react';
import { NativeModules } from 'react-native';
const { ReactNativeNativeNavigation } = NativeModules;
import SingleView from './../SingleView';
import StackView from './../stack/StackView';

export default class StackNavigation {
	screenID;

	constructor(screenID = '') {
		this.screenID = screenID;
	}

	handleBackButton = (callback) => {
		ReactNativeNativeNavigation.handleBackButton(callback);
	}

	push = (screen) => {
		// const Navigator = todo SingleView is assumed
		const Screen = <SingleView screen={screen.type} />;
		return ReactNativeNativeNavigation.push(Screen.type.mapToDictionary(StackView, this.screenID, Screen));
	}
}
