import React, { Component } from 'react';
import { AppRegistry } from 'react-native';
import ReactNativeNativeNavigation from './../ReactNativeNativeNavigation';
import SingleView from './single/SingleView';
import StackView from './stack/StackView';
import TabView from './tab/TabView';
import SplitView from './split/SplitView';
import DrawerView from './drawer/DrawerView';

class Navigation extends Component {

	state = {
		loading: true,
	};
	viewMap = {
		[SingleView.name]: SingleView,
		[StackView.name]: StackView,
		[TabView.name]: TabView,
		[SplitView.name]: SplitView,
		[DrawerView.name]: DrawerView,
	};
	pageMap = null;

	static getNode = (dom) => {
		const domType = dom.type;
		if (domType && typeof(domType.mapToDictionary) === 'function') {
			return dom;
		} else if (domType) {
			let Component = new domType();
			if (typeof(Component.render) === 'function') {
				let ComponentRender = Component.render();
				return Navigation.getNode(ComponentRender);
			}
		}
		console.error('RNNN', 'All children of Navigation need to support mapToDictionary');
		return null;
	};

	static mapChild = (dom, path) => {
		const node = Navigation.getNode(dom);
		return node.type.mapToDictionary(node, path);
	};
	mapChild = (dom, path) => Navigation.mapChild(dom, path);

	registerScreens = (screens) => {
		screens.forEach((screenData) => {
			const { screenID, screen } = screenData;
			const Screen = screen;
			AppRegistry.registerComponent(screenID, () => {
				const nav = this;
				return class extends Component {
					render() {
						return (
							<Screen navigation={nav}/>
						)
					}
				}
			});
		});
	};

	generateSiteMap = () => {
		const dom = this.props.children[1];
		return this.mapChild(dom, '');
	};

	/**
	 * Method that returns a list of a page and all pages listed inside a page.
	 * @param page
	 * @return {*[]}
	 */
	generatePageList = (page) => {
		if (page.pageMap) {
			return [
				...page.pageMap.reduce((array, innerPage) => {
					return [...array, ...this.generatePageList(innerPage)];
				}, []),
				page,
			]
		} else {
			return [page];
		}
	};

	componentDidMount() {
		/**
		 * Generate the pageMap, collect all used pages to be able to generate them.
		 */
		this.pageMap = this.props.pages.reduce((array, page) =>
				[...array, ...this.generatePageList(page)],
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
		if (this.props.customViews) {
			this.viewMap = {
				...this.viewMap,
				...this.props.customViews.reduce((map, page) => {
					return {
						...map,
						[page.name]: page,
					}
				}, {})
			};
		}

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
				request = this.generateSiteMap();
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
				this.registerScreens(screens);

				/**
				 * Everything is prepared to render natively.
				 */
				ReactNativeNativeNavigation.setSiteMap(request).then((loaded) => {
					this.setState({ loading: !loaded });
				});
			}
		});
	}

	render() {
		return this.props.children[0]
	}
}

module.exports = {
	Navigation,
};

export {
	Navigation,
}
