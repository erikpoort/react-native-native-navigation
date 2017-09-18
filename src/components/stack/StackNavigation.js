import React from 'react';
import { NativeModules } from 'react-native';

const { ReactNativeNativeNavigation } = NativeModules;
import { Navigation } from './../Navigation';
import SingleView from './../single/SingleView';
import StackView from './../stack/StackView';
import { Text } from 'react-native';

export default class StackNavigation {
	screenID;

	constructor(screenID) {
		this.screenID = screenID;
	}

	handleBackButton = (callback) => {
		ReactNativeNativeNavigation.handleBackButton(callback);
	}

	push = (screen) => {
		const Screen = <SingleView screen={screen.type}/>;
		const screenData = Screen.type.mapToDictionary(Screen, this.screenID);

		return ReactNativeNativeNavigation.push(screenData, (register) => {
			const view = Navigation.viewMap[register.type];
			const registerScreens = view.reduceScreens(register, Navigation.viewMap, Navigation.pageMap).filter((screen) => {
				return screen.screenID === screenData.screenID;
			});
			const registerScreenData = registerScreens[0];
			Navigation.registerScreen(registerScreenData.screenID, registerScreenData.screen);
		});
	}
}
