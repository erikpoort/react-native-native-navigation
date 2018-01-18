import React, { Component } from 'react';
import { AppRegistry } from 'react-native';

/**
 * Method that returns a list of a page and all pages listed inside a page.
 * @param page
 * @return {*[]}
 */
export const generatePageList = (page) => {
	if (page.pageMap) {
		return [
			...page.pageMap.reduce((array, innerPage) => {
				return [...array, ...generatePageList(innerPage)];
			}, []),
			page,
		]
	} else {
		return [page];
	}
};

export const registerScreens = (navigation, screens) => {
	const ReduxProvider = navigation.provider;
	const ReduxStore = navigation.store;

	screens.forEach((screenData) => {
		const { screenID, screen } = screenData;
		const Screen = screen;

		AppRegistry.registerComponent(screenID, () => {
			return class extends Component {
				renderScreen = () => <Screen navigation={navigation}/>;

				render() {
					return ReduxProvider && ReduxStore
						? <ReduxProvider store={ReduxStore}>{this.renderScreen()}</ReduxProvider>
						: this.renderScreen();
				}
			}
		});
	});
};

export const getNode = (dom) => {
	const domType = dom.type;
	if (domType && typeof(domType.mapToDictionary) === 'function') {
		return dom;
	} else if (domType) {
		let Component = new domType();
		if (typeof(Component.render) === 'function') {
			let ComponentRender = Component.render();
			return getNode(ComponentRender);
		}
	}
	console.error('RNNN', 'All children of Navigation need to support mapToDictionary');
	return null;
};

export const mapChild = (dom, path) => {
	const node = getNode(dom);
	return node.type.mapToDictionary(node, path);
};