import React, { Component } from 'react';
import { mapChild } from '../../utils/NavigationUtils';

export default class TabView extends Component {
	static mapChildren = (children, path) => {
		if (!Array.isArray(children)) {
			children = [children];
		}

		const leni = children.length;
		let nodes = [];
		for (let i = 0; i < leni; ++i) {
			const dom = children[i];
			const node = mapChild(dom, path);
			if (node == null) {
				return null;
			}
			nodes.push(node);
		}
		return nodes;
	};
	static mapToDictionary = (dom, path) => {
		if (dom == null || dom.props == null || path == null) {
			console.error("RNNN", "dom and path are mandatory parameters.");
			return null;
		}

		const id = dom.props.id;
		if (id == null) {
			console.error("RNNN", "An id prop is mandatory");
			return null;
		}

		const screenID = `${path}/${id}`
		const type = dom.type.name;

		if (dom.props.children.length < 2) {
			console.error("RNNN", "A TabView expects at least two children", screenID);
			return null;
		}

		const tabs = TabView.mapChildren(dom.props.children, screenID);
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
	};

	static reduceScreens = (data, viewMap, pageMap) => {
		return data.tabs.reduce((map, node) => {
			const viewData = viewMap[node.type];
			if (viewData) {
				const result = viewData.reduceScreens(node, viewMap, pageMap).map((view) => {
					const { screenID, screen } = view;
					const TabScreen = () => {
						return class extends Component {
							render() {
								const Screen = screen;
								return <Screen {...this.props} />
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
	}
}