import React, { Component } from 'react';
import SplitNavigation from './SplitNavigation';
import { mapChild } from '../../utils/NavigationUtils';

export const AXIS = {
	HORIZONTAL: "horizontal",
	VERTICAL: "vertical",
};

class SplitView {
	static AXIS = AXIS;
}

export const SplitNode = {
	[SplitView.name]: {
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

			if (dom.props.children.length !== 2) {
				console.error("RNNN", "A SplitView expects two children", screenID);
				return null;
			}

			const dom1 = dom.props.children[0];
			const node1 = mapChild(viewMap, dom1, screenID);
			const dom2 = dom.props.children[1];
			const node2 = mapChild(viewMap, dom2, screenID);

			if (node1 == null || node2 == null) {
				console.error("RNNN", "A SplitView expects two valid nodes", screenID);
				return null;
			}

			const axis = dom.props.axis;
			return {
				type,
				screenID,
				node1,
				node2,
				axis,
			}
		},
		reduceScreens: (data, viewMap, pageMap) => {
			const navigatorID = data.screenID;
			const navigatorName = navigatorID.split("/").pop();
			return [data.node1, data.node2].reduce((map, node) => {
				const viewData = viewMap[node.type];
				if (viewData) {
					const result = viewData.reduceScreens(node, viewMap, pageMap).map((view) => {
						const { screenID, screen } = view;
						const SplitScreen = () => {
							return class extends Component {
								split;

								render() {
									if (this.split == null) {
										this.split = new SplitNavigation(screenID, navigatorID, this.props.navigation);
									}

									const Screen = screen;
									return <Screen {...{
										[navigatorName]: this.split,
										split: this.split,
									}} {...this.props} />
								}
							}
						};

						const split = SplitScreen();
						return ({
							screenID,
							screen: split,
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
		},
	}
};

export default SplitView;