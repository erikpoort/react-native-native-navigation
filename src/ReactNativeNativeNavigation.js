import { NativeModules } from 'react-native';
const { ReactNativeNativeNavigation } = NativeModules;

onStart = () => {
	return ReactNativeNativeNavigation.onStart();
}

setSiteMap = (map) => {
	return ReactNativeNativeNavigation.setSiteMap(map);
}

handleBackButton = (callback) => {
	return ReactNativeNativeNavigation.handleBackButton(callback);
}

module.exports = {
	onStart,
	setSiteMap,
	handleBackButton,
}