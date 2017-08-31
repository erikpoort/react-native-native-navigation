import React from 'react';
import { NativeModules } from 'react-native';
const { ReactNativeNativeNavigation } = NativeModules;
import SingleView from './../SingleView';

handleBackButton = (callback) => {
	return ReactNativeNativeNavigation.handleBackButton(callback);
}

push = (screen) => {
	const Wrapper = <SingleView screen={screen.type} />
	return ReactNativeNativeNavigation.push(SingleView.mapToDictionary(SingleView, '', Wrapper));
}

module.exports = {
	handleBackButton,
	push,
}
