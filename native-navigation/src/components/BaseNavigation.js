export default class BaseNavigation {
	navigation;
	navigatorID;
	screenID;

	constructor(screenID, navigatorID, navigation) {
		this.screenID = screenID;
		this.navigatorID = navigatorID;
		this.navigation = navigation;
	}

	registerScreens(newPath, presentMethod, showScreen) {
		const Screen = showScreen;

		const screenData = this.navigation.mapChild(Screen, newPath);

		return presentMethod(screenData, (register) => {
			const viewMap = this.navigation.viewMap;
			const view = viewMap[register.type];
			const registerScreens = view.reduceScreens(register, viewMap, this.navigation.pageMap).filter((screen) => {
				return screen.screenID.includes(newPath) && screen.screenID !== newPath;
			});
			this.navigation.registerScreens(registerScreens);
		})
	}
}