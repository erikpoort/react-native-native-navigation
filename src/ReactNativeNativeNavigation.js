import { NativeModules } from 'react-native';
const { ReactNativeNativeNavigation } = NativeModules;

onStart = (callback) => {
	ReactNativeNativeNavigation.onStart(callback);
}

setSiteMap = (map) => {
	return ReactNativeNativeNavigation.setSiteMap(map);
}

module.exports = {
	onStart,
	setSiteMap,
}