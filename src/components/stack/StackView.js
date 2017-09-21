import React, { Component } from 'react';
import { BackHandler } from 'react-native';
import StackNavigation from './StackNavigation';
import { Navigation } from './../Navigation';

export default class StackView extends Component {
	static mapChildren = (children, path) => {
		if (!Array.isArray(children)) {
			children = [children];
		}
		let buildPath = path;

		const leni = children.length;
		let nodes = [];
		for (let i = 0; i < leni; ++i) {
			const dom = children[i];
			const node = Navigation.mapChild(dom, buildPath);
			if (node == null) {
				return null;
			}
			buildPath = node.screenID;
			nodes.push(node);
		}
		return nodes;
	}
	static mapToDictionary = (dom, path) => {
		if (dom == null || dom.props == null || path == null) {
			console.error("RNNN", "dom and path are mandatory parameters.");
			return null;
		}

		const name = dom.props.name;
		if (name == null) {
			console.error("RNNN", "A name prop is mandatory");
			return null;
		}

		const screenID = `${path}/${name}`
		const type = dom.type.name;

		if (dom.props.children.length == 0) {
			console.error("RNNN", "A StackView expects at least one child", screenID);
			return null;
		}

		const stack = dom.type.mapChildren(dom.props.children, screenID);
		if (stack == null) {
			console.error("RNNN", "A StackView expects all children to be valid nodes", screenID);
			return null;
		}

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
