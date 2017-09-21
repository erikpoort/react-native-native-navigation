import React, { Component } from 'react';
import SingleNavigation from './SingleNavigation';

export default class SingleView extends Component {
	static mapToDictionary = (dom, path) => {
		if (dom == null || dom.props == null || path == null) {
			console.error("RNNN", "dom and path are mandatory parameters.");
			return null;
		}

		const { name, screen, modal } = dom.props;

		if (name == null) {
			console.error("RNNN", "A name prop is mandatory");
			return null;
		}
		if (screen == null) {
			console.error("RNNN", "A screen prop is mandatory in SingleView", screenID);
			return null;
		}

		const screenID = `${path}/${name}`;
		const type = dom.type.name;

		let modalData = null;
		if (modal) {
			modalData = modal.type.mapToDictionary(modal, `${screenID}/modal`, screenID);
		}

		return {
			name,
			type,
			screenID,
			modal: modalData,
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
