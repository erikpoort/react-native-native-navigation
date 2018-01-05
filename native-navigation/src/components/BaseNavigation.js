export default class BaseNavigation{
    screenID;
    navigation;

    constructor(screenID, navigation) {
        this.screenID = screenID;
        this.navigation = navigation;
    }

    registerScreens(appendPath, presentMethod, showScreen) {
        const Screen = showScreen;
        const newPath = `${this.screenID}${appendPath}`;

        const screenData = this.navigation.mapChild(Screen, newPath);

        return presentMethod(screenData, (register) => {
            const viewMap = this.navigation.viewMap;
            const view = viewMap[register.type];
            const registerScreens = view.reduceScreens(register, viewMap, this.navigation.pageMap).filter((screen) => {
                return screen.screenID.includes(this.screenID) && screen.screenID !== this.screenID;
            });
            this.navigation.registerScreens(registerScreens);
        })
    }
}