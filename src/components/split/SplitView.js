import React, { Component } from 'react';
import { Navigation } from './../Navigation';

export default class SplitView extends Component {
	static AXIS = {
		HORIZONTAL: "horizontal",
		VERTICAL: "vertical",
	}

	static mapToDictionary = (dom, path) => {
		const type = dom.type.name;
		const screenID = `${path}/${dom.props.name}`;
		const dom1 = dom.props.children[0];
		const node1 = Navigation.mapChild(dom1, screenID);
		const dom2 = dom.props.children[1];
		const node2 = Navigation.mapChild(dom2, screenID);
		const axis = dom.props.axis;
		return {
			type,
			screenID,
			node1,
			node2,
			axis,
		}
	}

	static reduceScreens = (data, viewMap, pageMap) => {
		return [data.node1, data.node2].reduce((map, node) => {
			const viewData = viewMap[node.type];
			if (viewData) {
				const result = viewData.reduceScreens(node, viewMap, pageMap).map((view) => {
					const { screenID, screen } = view;
					const SplitScreen = () => {
						return class extends Component {
							render() {
								const Screen = screen;
								return <Screen {...this.props} />
							}
						}
					}
					const Split = SplitScreen();
					return ({
						screenID,
						screen: Split,
					})
				});
				return [
					...map,
					...result,
				]
			} else {
				return null;
			}
		}, []).filter((screen) => screen != null);
	}
}