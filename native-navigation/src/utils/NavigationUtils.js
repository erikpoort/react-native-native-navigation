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

export const getNode = (viewMap, dom) => {
	const domType = dom.type.name;
	const node = viewMap[domType];

	if (node && typeof(node.mapToDictionary) === 'function') {
		return node;
	}

	console.error('RNNN', 'All children of Navigation need to support mapToDictionary');
	return null;
};

export const mapChild = (viewMap, dom, path) => {
	const node = getNode(viewMap, dom);
	return node.mapToDictionary(dom, path);
};