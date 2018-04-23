import React from 'react';
import NativeBridge from './NativeBridge';
import { generatePageList, mapChild, registerScreens, pageName } from './utils/NavigationUtils';
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
				[pageName(page)]: page,
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

	/**
	 * Call this method to start the app. Or to replace the current navigation.
	 * @param node  The node structure to show.
	 * @param reset If set to true, native state is replaced by the sent node.
	 */
	start(node, reset = false) {
		/**
		 * This method is called every time the app is refreshed.
		 * The native side will handle check for a cached state before rendering.
		 */
		NativeBridge.onStart((request) => {
			/**
			 * If the request is set, this means the native side has a saved state which needs to be
			 * rendered. Otherwise we need to generate a new one.
			 */
			if (!request || reset) {
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
				registerScreens(this, screens);

				/**
				 * Everything is prepared to render natively.
				 */
				NativeBridge.setSiteMap(request);
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
