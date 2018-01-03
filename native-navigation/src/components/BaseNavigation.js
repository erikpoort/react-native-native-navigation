import { NativeModules } from 'react-native';

export default class BaseNavigation{
    screenID;
    navigation;

    constructor(screenID, navigation) {
        this.screenID = screenID;
        this.navigation = navigation;
    }

    registerScreens(presentMethod, showScreen) {
        const Screen = showScreen;

        const screenData = this.navigation.mapChild(Screen, this.screenID);

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