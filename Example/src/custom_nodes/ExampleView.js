import React from 'react';
import { SingleNode } from 'react-native-native-navigation';

class ExampleView {}

export const ExampleNode = {
	[ExampleView.name]: {
		reduceScreens: SingleNode.SingleView.reduceScreens,
		mapToDictionary: SingleNode.SingleView.mapToDictionary,
	}
};

export default ExampleView;
