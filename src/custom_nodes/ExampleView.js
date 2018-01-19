import React from 'react';
import { SingleNode } from '../../native-navigation/src/components/single/SingleView';

class ExampleView {}

export const ExampleNode = {
	[ExampleView.name]: {
		reduceScreens: SingleNode.SingleView.reduceScreens,
		mapToDictionary: SingleNode.SingleView.mapToDictionary,
	}
};

export default ExampleView;
