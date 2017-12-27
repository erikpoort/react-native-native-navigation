import { NativeModules } from 'react-native';
import { Navigation } from './Navigation';
const { ReactNativeNativeNavigation } = NativeModules;

export default class BaseNavigation{

    registerScreens(modalPath, presentMethod, showScreen) {
        const Screen = showScreen;
        
        const screenData = Navigation.mapChild(Screen, modalPath);

        return presentMethod(screenData, (register) => {
            const view = Navigation.viewMap[register.type];
            const registerScreens = view.reduceScreens(register, Navigation.viewMap, Navigation.pageMap).filter((screen) => {
                return screen.screenID.includes(this.screenID) && screen.screenID !== this.screenID;
            });
            Navigation.registerScreens(registerScreens);
        })
    }
}