import { NativeModules } from 'react-native';
const { ReactNativeNativeNavigation } = NativeModules;

onStart = () => {
	return ReactNativeNativeNavigation.onStart();
}

setSiteMap = (map) => {
	return ReactNativeNativeNavigation.setSiteMap(map);
}

module.exports = {
	onStart,
	setSiteMap,
}