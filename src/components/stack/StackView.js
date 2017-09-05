import React, { Component } from 'react';
import { BackHandler } from 'react-native';
import StackNavigation from './StackNavigation';

export default class StackView extends Component {
	static mapChildren = (children, path) => {
		if (!Array.isArray(children)) children = [children];
		let buildPath = path;
		return children.map(dom => {
			let child = dom.type.mapToDictionary(dom, buildPath);
			buildPath = child.screenID;
			return child;
		});
	}
	static mapToDictionary = (dom, path) => {
		const type = dom.type.name;
		const screenID = `${path}/${dom.props.name}`
		const stack = dom.type.mapChildren(dom.props.children, screenID);
		return {
			type,
			screenID,
			stack,
		};
	}

	static reduceScreens = (data, viewMap, pageMap) => {
		return data.stack.reduce((map, node) => {
			const viewData = viewMap[node.type];
			if (viewData) {
				const result = viewData.reduceScreens(node, viewMap, pageMap).map((view) => {
					const { screenID, screen } = view;
					const StackScreen = () => {
						return class extends Component {
							removeBackButtonListener;
							stack;
							constructor() {
								super();
								this.stack = new StackNavigation(screenID);
							}
							componentWillMount() {
								const { remove } = BackHandler.addEventListener('hardwareBackPress', () => {
									this.stack.handleBackButton((handled) => {
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
							componentWillReceiveProps(newProps) {
							}
							render() {
								const Screen = screen;
								return <Screen stack={this.stack} {...this.props} />
							}
						}
					}
					const stack = StackScreen();
					return ({
						screenID,
						screen: stack,
					});
				});
				return [
					...map,
					...result,
				];
			}
			return map;
		}, []);
	}
}
