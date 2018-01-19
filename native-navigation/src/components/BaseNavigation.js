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

	addScreens(newPath, presentMethod, showScreen) {
		const Screen = showScreen;

		const viewMap = this.navigation.viewMap;
		const screenData = mapChild(viewMap, Screen, newPath);

		return presentMethod(screenData, (register) => {
			const view = viewMap[register.type];
			const screens = view.reduceScreens(register, viewMap, this.navigation.pageMap).filter((screen) => {
				return screen.screenID.includes(newPath) && screen.screenID !== newPath;
			});
			registerScreens(this.navigation, screens);
		})
	}
}