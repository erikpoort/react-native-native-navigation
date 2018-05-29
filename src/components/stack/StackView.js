import React, { Component } from 'react';
import { BackHandler } from 'react-native';
import StackNavigation from './StackNavigation';
import { mapChild } from '../../utils/NavigationUtils';

class StackView {
	static viewName = "StackView"
};

const mapChildren = (viewMap, children, path) => {
	if (!Array.isArray(children)) {
		children = [children];
	}
	let buildPath = path;

	const leni = children.length;
	let nodes = [];
	for (let i = 0; i < leni; ++i) {
		const dom = children[i];
		const node = mapChild(viewMap, dom, buildPath);
		if (node == null) {
			return null;
		}
		buildPath = node.screenID;
		nodes.push(node);
	}
	return nodes;
};

export const StackNode = {
	[StackView.viewName]: {
		mapToDictionary: (viewMap, dom, path) => {
			if (dom == null || dom.props == null || path == null) {
				console.error("RNNN", "dom and path are mandatory parameters.");
				return null;
			}

			const id = dom.props.id;
			if (id == null) {
				console.error("RNNN", "An id prop is mandatory");
				return null;
			}

			const screenID = `${path}/${id}`;
			const type = dom.type.viewName;

			if (dom.props.children.length === 0) {
				console.error("RNNN", "A StackView expects at least one child", screenID);
				return null;
			}

			const stack = mapChildren(viewMap, dom.props.children, screenID);
			if (stack == null) {
				console.error("RNNN", "A StackView expects all children to be valid nodes", screenID);
				return null;
			}

			return {
				type,
				screenID,
				stack,
			};
		},
		reduceScreens: (data, viewMap, pageMap) => {
			const navigatorID = data.screenID;
			const navigatorName = navigatorID.split("/").pop();
			return data.stack.reduce((map, node) => {
				const viewData = viewMap[node.type];
				if (viewData) {
					const result = viewData.reduceScreens(node, viewMap, pageMap).map((view) => {
						const { screenID, screen } = view;
						const StackScreen = () => {
							return class extends Component {
								removeBackButtonListener;
								stack;

								componentDidMount() {
									const { remove } = BackHandler.addEventListener('hardwareBackPress', () => {
										this.stack.handleBackButton((handled) => {
											if (!handled) {
												BackHandler.exitApp();
											}
										});
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
									if (this.stack == null) {
										this.stack = new StackNavigation(screenID, navigatorID, this.props.navigation);
									}

									const Screen = screen;
									return <Screen {...{
										[navigatorName]: this.stack,
										stack: this.stack
									}} {...this.props} />
								}
							}
						};

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
		},
	}
};

export default StackView;
