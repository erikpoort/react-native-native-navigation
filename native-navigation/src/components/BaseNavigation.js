import { registerScreens, mapChild } from '../utils/NavigationUtils';

export default class BaseNavigation {
	navigation;
	navigatorID;
	screenID;

	constructor(screenID, navigatorID, navigation) {
		this.navigation = navigation;
		this.navigatorID = navigatorID;
		this.screenID = screenID;
	}

	registerScreens(newPath, presentMethod, showScreen) {
		const Screen = showScreen;

		const screenData = mapChild(Screen, newPath);

		return presentMethod(screenData, (register) => {
			const viewMap = this.navigation.viewMap;
			const view = viewMap[register.type];
			const screens = view.reduceScreens(register, viewMap, this.navigation.pageMap).filter((screen) => {
				return screen.screenID.includes(newPath) && screen.screenID !== newPath;
			});
			registerScreens(this.navigation, screens);
		})
	}
}