import React, { Component } from 'react';
import { AppRegistry } from 'react-native';

const registerScreen = (navigator, screenID, screen) => {
	AppRegistry.registerComponent(screenID, () => {
		const Screen = screen;
		Screen.screenID = screenID;
		const Navigator = navigator;
		return class extends Component {
			render() {
				const props = this.props;
				return (
					<Navigator screen={Screen} />
				)
			}
		}
	});
}

export default class SingleView extends Component {
	static mapToDictionary = (navigator, path, dom) => {
		const { screen } = dom.props;
		const name = screen.name;
		const type = dom.type.name;
		const screenID = `${path}/${name}`;
		const parentType = navigator.name;

		registerScreen(navigator, screenID, screen);
		return {
			name,
			type,
			parentType,
			screenID,
		};
	};

	static handleMap = (data, viewMap, pageMap) => {
		const navigator = viewMap[data.parentType];
		const screenID = data.screenID;
		const screen = pageMap[data.name];
		registerScreen(navigator, screenID, screen);
	}

	screenID;

	render() {
		const Screen = this.props.screen;
		return <Screen stack={this.props.stack} />;
	}
}
