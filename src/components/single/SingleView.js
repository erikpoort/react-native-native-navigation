import React, { Component } from 'react';
import SingleNavigation from './SingleNavigation';

export default class SingleView extends Component {
	static mapToDictionary = (dom, path) => {
		const { screen, modal } = dom.props;
		const type = dom.type.name;
		const name = screen.name;
		const screenID = `${path}/${name}`;

		let modalData = {};
		if (modal) {
			modalData = { modal: modal.type.mapToDictionary(modal, screenID) };
		}
		return {
			name,
			type,
			screenID,
			...modalData,
		};
	};

	static reduceScreens = (data, viewMap, pageMap) => {
		const screenID = data.screenID;
		const SingleScreen = (screen) => {
			const Screen = screen;
			return class extends Component {
				single;
				constructor() {
					super();
					this.single = new SingleNavigation(screenID);
				}
				render() {
					return <Screen single={this.single} {...this.props} />;
				}
			}
		};
		const screen = SingleScreen(pageMap[data.name]);

		let modal = [];
		if (data.modal) {
			const modalData = viewMap[data.modal.type];
			modal = modalData.reduceScreens(data.modal, viewMap, pageMap);
		}

		return [{
			screenID,
			screen,
		}, ...modal];
	}
}
