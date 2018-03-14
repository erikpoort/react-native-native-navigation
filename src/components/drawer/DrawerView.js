import React, { Component } from 'react';
import DrawerNavigation from "./DrawerNavigation";
import { mapChild } from '../../utils/NavigationUtils';

class DrawerView {}

const nodeToDictionary = (viewMap, name, dom, path) => {
	let data = dom.props[name];
	if (data !== null && data !== undefined) {
		return mapChild(viewMap, data, `${path}/${name}`);
	}
	return null;
};

export const DrawerNode = {
	[DrawerView.name]: {
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
			const type = dom.type.name;

			const center = nodeToDictionary(viewMap, 'center', dom, screenID);
			if (center == null) {
				console.error('RNNN', 'A center node is mandatory in a Drawer', screenID);
				return null;
			}

			const left = nodeToDictionary(viewMap, 'left', dom, screenID);
			const right = nodeToDictionary(viewMap, 'right', dom, screenID);
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
		},
		reduceScreens: (data, viewMap, pageMap) => {
			const navigatorID = data.screenID;
			const navigatorName = navigatorID.split("/").pop();
			return [data.left, data.center, data.right].filter((side) => {
				return side !== undefined && side !== null;
			}).reduce((map, node) => {
				const viewData = viewMap[node.type];
				if (viewData) {
					const result = viewData.reduceScreens(node, viewMap, pageMap).map((view) => {
						const { screenID, screen } = view;
						const DrawerScreen = () => {
							return class extends Component {
								drawer;

								componentWillMount() {
									this.drawer = new DrawerNavigation(screenID, navigatorID, this.props.navigation);
									this.drawer.drawerView = screen
								}

								render() {
									const Screen = screen;
									return <Screen {...{
										[navigatorName]: this.drawer,
										drawer: this.drawer
									}} {...this.props} />
								}
							}
						};
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
					return map;
				}
			}, []).filter((screen) => screen != null);
		},
	}
};

export default DrawerView;
