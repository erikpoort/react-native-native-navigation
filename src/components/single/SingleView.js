import React, { Component } from 'react';
import SingleNavigation from './SingleNavigation';
import { mapChild, pageName } from '../../utils/NavigationUtils';

class SingleView {
	static viewName = "SingleView"
}

export const SingleNode = {
	[SingleView.viewName]: {
		mapToDictionary: (viewMap, dom, path) => {
			if (dom == null || dom.props == null || path == null) {
				console.error("RNNN", "dom and path are mandatory parameters.");
				return null;
			}

			const { id, screen, modal, style, passProps } = dom.props;

			if (id == null) {
				console.error("RNNN", "An id prop is mandatory");
				return null;
			}

			const screenID = `${path}/${id}`;

			if (screen == null) {
				console.error("RNNN", "A screen prop is mandatory in SingleView", screenID);
				return null;
			}

			const type = dom.type.viewName;
			const page = pageName(screen);

			if (passProps != null) {
				const acceptedTypes = ["boolean", "number", "string"]
				for (var key in passProps) {
					if (passProps.hasOwnProperty(key)) {
						var keyType = typeof key
						var valueType = typeof passProps[key]
						if (!acceptedTypes.includes(valueType) || !acceptedTypes.includes(keyType)) {
							console.error("RNNN", "You can only pass strings, numbers and booleans. this is to " +
								"avoid bridge abuse. If you need to pass small objects, split them up into key " +
								"value. If you need to pass large objects concider saving them in something like " +
								"a redux store and pass the id of the object.", screenID);
							return null;
						}
					}
				}
			}

			let modalData = null;
			if (modal) {
				modalData = mapChild(viewMap, modal, `${screenID}/modal`);
			}
			return {
				page,
				type,
				screenID,
				style,
				modal: modalData,
				passProps,
			};
		},
		reduceScreens: (data, viewMap, pageMap) => {
			const screenID = data.screenID;
			const screenName = data.screenID.split("/").pop();
			const SingleScreen = (screen) => {
				const Screen = screen;
				return class extends Component {
					single;

					render() {
						if (this.single == null) {
							this.single = new SingleNavigation(screenID, screenID, this.props.navigation);
						}

						return <Screen {...{
							[screenName]: this.single,
							single: this.single
						}} {...this.props} />;
					}
				}
			};
			const screen = SingleScreen(pageMap[data.page]);

			let modal = [];
			if (data.modal) {
				const modalData = viewMap[data.modal.type];
				modal = modalData.reduceScreens(data.modal, viewMap, pageMap);
			}

			return [{
				screenID,
				screen,
			}, ...modal];
		},
	}
};

export default SingleView;
