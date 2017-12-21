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

	showModal = (showScreen) => {
		const Screen = showScreen;
		const screenData = Navigation.mapChild(Screen, `${this.screenID}/modal`);

		return ReactNativeNativeNavigation.showModal(screenData, (register) => {
			const view = Navigation.viewMap[register.type];
			const registerScreens = view.reduceScreens(register, Navigation.viewMap, Navigation.pageMap).filter((screen) => {
				return screen.screenID.includes(this.screenID) && screen.screenID !== this.screenID;
			});
			Navigation.registerScreens(registerScreens);
		});
	}
}