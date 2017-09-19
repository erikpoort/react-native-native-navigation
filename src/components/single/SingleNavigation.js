import React from 'react';
import { NativeModules } from 'react-native';

const { ReactNativeNativeNavigation } = NativeModules;
import { Navigation } from './../Navigation';
import StackView from './../stack/StackView';
import SingleView from './../single/SingleView';

export default class SingleNavigation {
	screenID;

	constructor(screenID) {
		this.screenID = screenID;
	}

	showModal = (screen) => {
		const Screen = <SingleView screen={screen}/>;
		const screenData = Screen.type.mapToDictionary(Screen, this.screenID);

		return ReactNativeNativeNavigation.showModal(screenData, (register) => {
			const view = Navigation.viewMap[register.type];
			const registerScreens = view.reduceScreens(register, Navigation.viewMap, Navigation.pageMap).filter((screen) => {
				return screen.screenID === screenData.screenID;
			});
			const registerScreenData = registerScreens[0];
			Navigation.registerScreen(registerScreenData.screenID, registerScreenData.screen);
		});
	}
}