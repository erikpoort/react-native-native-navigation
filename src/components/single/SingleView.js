import React, { Component } from 'react';
import SingleNavigation from './SingleNavigation';
import { mapChild, pageName } from '../../utils/NavigationUtils';

class SingleView {}

export const SingleNode = {
	[SingleView.name]: {
		mapToDictionary: (viewMap, dom, path) => {
			if (dom == null || dom.props == null || path == null) {
				console.error("RNNN", "dom and path are mandatory parameters.");
				return null;
			}

			const { id, screen, modal, style } = dom.props;

			if (id == null) {
				console.error("RNNN", "An id prop is mandatory");
				return null;
			}

			const screenID = `${path}/${id}`;

			if (screen == null) {
				console.error("RNNN", "A screen prop is mandatory in SingleView", screenID);
				return null;
			}

			const type = dom.type.name;
			const page = pageName(screen);

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
