import React from 'react';
import { NativeModules } from 'react-native';

const { ReactNativeNativeNavigation } = NativeModules;
import { Navigation } from './../Navigation';
import SingleView from './../single/SingleView';

export default class StackNavigation {
	screenID;

	constructor(screenID) {
		this.screenID = screenID;
	}

	handleBackButton = (callback) => {
		ReactNativeNativeNavigation.handleBackButton(callback);
	}

	push = (showScreen) => {
		const Screen = showScreen;
		const screenData = Navigation.mapChild(Screen, this.screenID);

		return ReactNativeNativeNavigation.push(screenData, (register) => {
			const view = Navigation.viewMap[register.type];
			const registerScreens = view.reduceScreens(register, Navigation.viewMap, Navigation.pageMap).filter((screen) => {
				return screen.screenID.includes(this.screenID) && screen.screenID !== this.screenID;
			});
			Navigation.registerScreens(registerScreens);
		});
	}
}
