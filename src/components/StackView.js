import React, { Component } from 'react';
import { BackHandler } from 'react-native';
import ReactNativeNativeNavigation from './../ReactNativeNativeNavigation';

export default class StackView extends Component {
	static mapChildren = (navigator, path, children) => {
		if (!Array.isArray(children)) children = [children];
		return children.map(dom => dom.type.mapToDictionary(navigator, path, dom));
	}
	static mapToDictionary = (navigator, path, dom) => {
		const type = dom.type.name;
		const stack = dom.type.mapChildren(navigator, path, dom.props.children);
		return {
			type,
			stack,
		};
	}

	removeBackButtonListener;
	componentWillMount() {
		const { remove } = BackHandler.addEventListener('hardwareBackPress', function() {
			ReactNativeNativeNavigation.handleBackButton(handled => {
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
		return this.props.children;
	}
}
