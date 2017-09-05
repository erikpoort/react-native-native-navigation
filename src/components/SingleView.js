import React, { Component } from 'react';

export default class SingleView extends Component {
	static mapToDictionary = (dom, path) => {
		const { screen } = dom.props;
		const type = dom.type.name;
		const name = screen.name;
		const screenID = `${path}/${name}`;
		return {
			name,
			type,
			screenID,
		};
	};

	static reduceScreens = (data, viewMap, pageMap) => {
		const screenID = data.screenID;
		const SingleScreen = (screen) => {
			const Screen = screen;
			return class extends Component {
				render() {
					return <Screen {...this.props} />;
				}
			}
		}
		const screen = SingleScreen(pageMap[data.name]);
		return [{
			screenID,
			screen,
		}]
	}
}
