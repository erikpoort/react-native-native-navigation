import React, { Component } from 'react';
import { BackHandler } from 'react-native';
import StackNavigation from './StackNavigation';
import SingleView from './../SingleView';

export default class StackView extends Component {
	static mapChildren = (navigator, path, children) => {
		if (!Array.isArray(children)) children = [children];
		let buildPath = path;
		return children.map(dom => {
			let child = dom.type.mapToDictionary(navigator, buildPath, dom);
			buildPath = child.screenID;
			return child;
		});
	}
	static mapToDictionary = (navigator, path, dom) => {
		const type = dom.type.name;
		const stack = dom.type.mapChildren(navigator, path, dom.props.children);
		return {
			type,
			stack,
		};
	}

	static handleMap = (data, viewMap, pageMap) => {
		data.stack.forEach(node => {
			const view = viewMap[node.type];
			if (view) {
				view.handleMap(node, viewMap, pageMap);
			}
		});
	}

	stackNavigation;
	removeBackButtonListener;

	componentWillMount() {
		const Screen = this.props.screen;
		this.stackNavigation = new StackNavigation(Screen.screenID);

		const { remove } = BackHandler.addEventListener('hardwareBackPress', () => {
			this.stackNavigation.handleBackButton((handled) => {
				if (!handled) {
					BackHandler.exitApp();
				}
			})
			return true;
		});
		this.removeBackButtonListener = remove;
	}

	componentWillUnmount() {
		if (this.removeBackButtonListener != null) {
			this.removeBackButtonListener();
		}
	}

	render() {
		const Screen = this.props.screen;
		return <Screen stack={this.stackNavigation} />;
	}
}
