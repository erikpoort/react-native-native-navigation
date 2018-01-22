import React from 'react';
import ReactNativeNativeNavigation from './ReactNativeNativeNavigation';
import { generatePageList, mapChild, registerScreens } from './utils/NavigationUtils';
import { SingleNode } from './components/single/SingleView';
import { StackNode } from './components/stack/StackView';
import { TabNode } from './components/tab/TabView';
import { SplitNode } from './components/split/SplitView';
import { DrawerNode } from './components/drawer/DrawerView';

class Navigation {

	provider = null;
	store = null;
	viewMap = {
		...SingleNode,
		...StackNode,
		...TabNode,
		...SplitNode,
		...DrawerNode,
	};
	pageMap = null;

	constructor(pages, customViews, provider = null, store = null) {
		this.provider = provider;
		this.store = store;

		/**
		 * Generate the pageMap, collect all used pages to be able to generate them.
		 */
		this.pageMap = pages.reduce((array, page) =>
				[...array, ...generatePageList(page)],
			[]
		).reduce((map, page) => {
			return {
				...map,
				[page.name]: page,
			}
		}, {});

		/**
		 * Check if there's any custom nodes added by the developer using the library.
		 */
		if (customViews) {
			this.viewMap = {
				...this.viewMap,
				...customViews.reduce((map, node) => ({...map, ...node}), {}),
			};
		}
	}

	start(node) {
		/**
		 * This method is called every time the app is refreshed.
		 * The native side will handle check for a cached state before rendering.
		 */
		ReactNativeNativeNavigation.onStart((request) => {
			/**
			 * If the request is set, this means the native side has a saved state which needs to be
			 * rendered. Otherwise we need to generate a new one.
			 */
			if (!request) {
				request = mapChild(this.viewMap, node, '');
			}

			if (request) {
				/**
				 * Request is an object containing a node of a certain node type.
				 * Find it's js class in the viewMap.
				 */
				const dom = this.viewMap[request.type];

				/**
				 * Use the first node to find all it's child nodes and return the screens as an array.
				 */
				const screens = dom.reduceScreens(request, this.viewMap, this.pageMap);

				/**
				 * Register all screens to be able to render them through RN.
				 */
				registerScreens(this, screens, this.provider, this.store);

				/**
				 * Everything is prepared to render natively.
				 */
				ReactNativeNativeNavigation.setSiteMap(request);
			}
		});
	}
}

module.exports = {
	Navigation,
};

export {
	Navigation,
}
