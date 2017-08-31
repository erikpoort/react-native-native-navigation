import React, { Component } from 'react';
import { registerScreen } from './Navigation';

export default class SingleView extends Component {

	static mapToDictionary = (path, dom) => {
		const { screen } = dom.props;
		const name = screen.name;
		const type = dom.type.name;
		const screenID = `${path}/${name}`;
		registerScreen(screenID, screen);
		return {
			name,
			type,
			screenID,
		};
	}
}
