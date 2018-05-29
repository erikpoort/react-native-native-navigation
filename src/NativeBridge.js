import { NativeModules, NativeEventEmitter } from 'react-native';
const { ReactNativeNativeNavigation, ReactNativeNativeEventEmitter } = NativeModules;

const eventEmitter = new NativeEventEmitter(ReactNativeNativeEventEmitter)

onStart = (callback) => {
	ReactNativeNativeNavigation.onStart(callback);
}

setSiteMap = (map) => {
	return ReactNativeNativeNavigation.setSiteMap(map);
}

module.exports = {
	eventEmitter,
	onStart,
	setSiteMap,
}
