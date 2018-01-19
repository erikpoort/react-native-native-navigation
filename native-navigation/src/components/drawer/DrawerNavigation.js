import React from 'react';
import { NativeModules } from 'react-native';

const { ReactNativeNativeNavigation } = NativeModules;

import BaseNavigation from '../BaseNavigation';

export default class DrawerNavigation extends BaseNavigation {
	openView = (side, showScreen) => {
		const components = this.screenID.split('/');

		let newPath = "";
		let closestSideComponentChanged = false
		for (let i = components.length - 1; i > 0; i--) {
			const comp = components.pop();
			if ((comp === "left" || comp === "center" || comp === "right")
				&& !closestSideComponentChanged) {
				newPath = `/${side}${newPath}`;
				closestSideComponentChanged = true;
			} else {
				newPath = `/${comp}${newPath}`
			}
		}

		return super.registerScreens(newPath, "openView", showScreen);
	}

}
