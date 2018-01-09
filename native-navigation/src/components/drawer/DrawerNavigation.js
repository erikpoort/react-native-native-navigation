import React from 'react';
import { NativeModules } from 'react-native';

const { ReactNativeNativeNavigation } = NativeModules;
import SingleView from './../single/SingleView';

import BaseNavigation from '../BaseNavigation';

export default class DrawerNavigation extends BaseNavigation{
    sayHi = (callback) => {
        ReactNativeNativeNavigation.testBridgeHola(callback)
    }
}
