import React, { Component } from 'react';
import { registerScreen } from './Navigation';

export default class SingleView extends Component {
	static mapToDictionary = (navigator, path, dom) => {
		const { screen } = dom.props;
		const name = screen.name;
		const type = dom.type.name;
		const screenID = `${path}/${name}`;
		registerScreen(navigator, screenID, screen);
		return {
			name,
			type,
			screenID,
		};
	}

	render() {
		return this.props.children;
	}
}
