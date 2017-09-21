import React, { Component } from 'react';
import { Navigation } from './../Navigation';

export default class DrawerView extends Component {
	static SIDE = {
		LEFT: "left",
		CENTER: "center",
		RIGHT: "right",
	}

	static nodeToDictionary = (name, dom, path) => {
		let result;
		let data = dom.props[name];
		if (data != null) {
			return Navigation.mapChild(data, `${path}/${name}`);
		}
		return null;
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

		const screenID = `${path}/${name}`;
		const type = dom.type.name;

		const center = DrawerView.nodeToDictionary('center', dom, screenID);
		if (center == null) {
			console.error('RNNN', 'A center node is mandatory in a Drawer', screenID);
			return null;
		}

		const left = DrawerView.nodeToDictionary('left', dom, screenID);
		const right = DrawerView.nodeToDictionary('right', dom, screenID);
		if (left == null && right == null) {
			console.error('RNNN', 'You need a left or right drawer', screenID);
			return null;
		}

		const side = dom.props.side;
		return {
			type,
			screenID,
			center,
			left,
			right,
			side
		}
	}

	static reduceScreens = (data, viewMap, pageMap) => {
		return [data.left, data.center, data.right].filter((side) => {
			return side != undefined;
		}).reduce((map, node) => {
			const viewData = viewMap[node.type];
			if (viewData) {
				const result = viewData.reduceScreens(node, viewMap, pageMap).map((view) => {
					const { screenID, screen } = view;
					const DrawerScreen = () => {
						return class extends Component {
							render() {
								const Screen = screen;
								return <Screen {...this.props} />
							}
						}
					}
					const Drawer = DrawerScreen();
					return ({
						screenID,
						screen: Drawer,
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