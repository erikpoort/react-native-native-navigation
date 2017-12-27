import { NativeModules } from 'react-native';
import StackNavigation from "./stack/StackNavigation";
const { ReactNativeNativeNavigation } = NativeModules;

export default class BaseNavigation{

    registerScreens(cls, Navigation, showScreen) {
        const Screen = showScreen;
        const modalPath = cls instanceof StackNavigation ? `${this.screenID}` : `${this.screenID}/modal`;
        const screenData = Navigation.mapChild(Screen, modalPath);

        const presentMethod = cls instanceof StackNavigation ? ReactNativeNativeNavigation.push : ReactNativeNativeNavigation.showModal

        return presentMethod(screenData, (register) => {
            const view = Navigation.viewMap[register.type];
                const registerScreens = view.reduceScreens(register, Navigation.viewMap, Navigation.pageMap).filter((screen) => {
                    return screen.screenID.includes(this.screenID) && screen.screenID !== this.screenID;
                });
                Navigation.registerScreens(registerScreens);
        })
    }
}