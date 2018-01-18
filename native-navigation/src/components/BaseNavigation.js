import { NativeModules } from 'react-native';

const { ReactNativeNativeNavigation } = NativeModules;

export default class BaseNavigation {
	navigation;
	navigatorID;
	screenID;

	constructor(screenID, navigatorID, navigation) {
		this.screenID = screenID;
		this.navigatorID = navigatorID;
		this.navigation = navigation;
	}

	registerScreens(newPath, presentMethod, showScreen){
		const args = {
			"screen": this.navigation.mapChild(showScreen, this.screenID),
		};
		return ReactNativeNativeNavigation.callMethodOnNode(newPath, this.navigatorID, presentMethod, args, (register) => {
			const viewMap = this.navigation.viewMap;
			const view = viewMap[register.type];
			const registerScreens = view.reduceScreens(register, viewMap, this.navigation.pageMap).filter((screen) => {
				return screen.screenID.includes(newPath) && screen.screenID !== newPath;
			});
			this.navigation.registerScreens(registerScreens);
		})
	}

	// registerScreens(newPath, presentMethod, showScreen) {
	// 	console.warn(this.navigatorID);
	// 	const Screen = showScreen;
	//
	// 	const screenData = this.navigation.mapChild(Screen, newPath);
	//
	// 	return presentMethod(screenData, (register) => {
	// 		const viewMap = this.navigation.viewMap;
	// 		const view = viewMap[register.type];
	// 		const registerScreens = view.reduceScreens(register, viewMap, this.navigation.pageMap).filter((screen) => {
	// 			return screen.screenID.includes(newPath) && screen.screenID !== newPath;
	// 		});
	// 		this.navigation.registerScreens(registerScreens);
	// 	})
	// }
}