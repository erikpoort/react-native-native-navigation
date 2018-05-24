import React, { Component } from 'react';
import { mapChild } from '../../utils/NavigationUtils';
import TabNavigation from './TabNavigation'

class TabView {
	static viewName = "TabView"
};

const mapChildren = (viewMap, children, path) => {
	if (!Array.isArray(children)) {
		children = [children];
	}

	const leni = children.length;
	let nodes = [];
	for (let i = 0; i < leni; ++i) {
		const dom = children[i];
		const node = mapChild(viewMap, dom, path);
		if (node == null) {
			return null;
		}
		nodes.push(node);
	}
	return nodes;
};

export const TabNode = {
	[TabView.viewName]: {
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

			if (dom.props.children.length < 2) {
				console.error("RNNN", "A TabView expects at least two children", screenID);
				return null;
			}

			const tabs = mapChildren(viewMap, dom.props.children, screenID);
			if (tabs == null) {
				console.error("RNNN", "A TabView expects all children to be valid nodes", screenID);
				return null;
			}

			const selectedTab = dom.props.selectedTab;
			return {
				type,
				screenID,
				tabs,
				selectedTab,
			};
		},
		reduceScreens: (data, viewMap, pageMap) => {
			const navigatorID = data.screenID;
			const navigatorName = navigatorID.split("/").pop();
			return data.tabs.reduce((map, node) => {
				const viewData = viewMap[node.type];
				if (viewData) {
					const result = viewData.reduceScreens(node, viewMap, pageMap).map((view) => {
						const { screenID, screen } = view;
						const TabScreen = () => {
							return class extends Component {
								tabs;

								render() {
									if (this.tabs == null) {
										this.tabs = new TabNavigation(screenID, navigatorID, this.props.navigation);
									}

									const Screen = screen;
									return <Screen {...{
										[navigatorName]: this.tabs,
										tabs: this.tabs
									}} {...this.props} />
								}
							}
						};
						const Tab = TabScreen();
						return ({
							screenID,
							screen: Tab,
						})
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

export default TabView;