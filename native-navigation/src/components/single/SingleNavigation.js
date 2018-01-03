import React from 'react';
import { NativeModules } from 'react-native';
import BaseNavigation from '../BaseNavigation';

const { ReactNativeNativeNavigation } = NativeModules;

export default class SingleNavigation extends BaseNavigation{
	showModal = (showScreen) => {
		return super.registerScreens('/modal', ReactNativeNativeNavigation.showModal, showScreen);
	}
}