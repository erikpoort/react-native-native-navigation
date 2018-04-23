import React from 'react';
import { NativeModules } from 'react-native';
import BaseNavigation from '../BaseNavigation';
import NativeBridge from '../../NativeBridge'

const { eventEmitter } = NativeBridge
const { ReactNativeNativeNavigation } = NativeModules;

export default class SingleNavigation extends BaseNavigation {
	listeners = {};

	constructor(screenID, navigatorID, navigation) {
		super(screenID, navigatorID, navigation)

		eventEmitter.addListener(screenID, (data) => {
			let listener = this.listeners[data]
			if (listener != null) {
				listener()
			}
		})
	}

	showModal = (showScreen) => {
		const newPath = `${this.screenID}/modal`;
		return super.addScreens(newPath, ReactNativeNativeNavigation.showModal, showScreen, null);
	};

	updateStyle = (style) => {
		ReactNativeNativeNavigation.callMethodOnNode(this.navigatorID, ReactNativeNativeNavigation.updateStyle, style, () => {});
	}

	addListener = (id, listener) => {
		this.listeners[id] = listener
	}
}
