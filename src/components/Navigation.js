import React, { Component } from 'react';
import { AppRegistry, View } from 'react-native';
import ReactNativeNativeNavigation from './../ReactNativeNativeNavigation';
import SingleView from './single/SingleView';
import StackView from './stack/StackView';
import TabView from './tab/TabView';
import SplitView from './split/SplitView';
import DrawerView from './drawer/DrawerView';

class Navigation extends Component {
	static pageMap;
	static viewMap;

	state = {
		loading: true,
	};

	static mapChild = (dom, path) => {
		if (dom.type && typeof(dom.type.mapToDictionary) === 'function') {
			return dom.type.mapToDictionary(dom, path);
		} else if (dom.type) {
			let ComponentClass = dom.type;
			let Component = new ComponentClass();
			if (typeof(Component.render) === 'function') {
				let ComponentRender = Component.render();
				return Navigation.mapChild(ComponentRender, path);
			}
		}
		console.error('RNNN', 'All children of Navigation need to support mapToDictionary');
		return null;
	};

	static registerScreen = (screenID, screen) => {
		const Screen = screen;
		AppRegistry.registerComponent(screenID, () => {
			return class extends Component {
				render() {
					return (
						<Screen/>
					)
				}
			}
		});
	};

	static registerScreens = (screens) => {
		screens.forEach((screenData) => {
			const { screenID, screen } = screenData;
			Navigation.registerScreen(screenID, screen)
		});
	};

	generateSiteMap = () => {
		const dom = this.props.children[1];
		return Navigation.mapChild(dom, '');
	};

	componentDidMount() {
		Navigation.pageMap = this.props.pages.reduce((array, page) => {
			if (page.pageMap) {
				return [
					...array,
					...page.pageMap,
					page,
				]
			} else {
				return [
					...array,
					page,
				];
			}
		}, []).reduce((map, page) => {
			return {
				...map,
				[page.name]: page,
			}
		}, {});
		Navigation.viewMap = {
			[SingleView.name]: SingleView,
			[StackView.name]: StackView,
			[TabView.name]: TabView,
			[SplitView.name]: SplitView,
			[DrawerView.name]: DrawerView,
		};

		ReactNativeNativeNavigation.onStart((request) => {
			if (!request) {
				request = this.generateSiteMap();
			}

			if (request) {
				const dom = Navigation.viewMap[request.type];
				const screens = dom.reduceScreens(request, Navigation.viewMap, Navigation.pageMap);
				Navigation.registerScreens(screens);

				ReactNativeNativeNavigation.setSiteMap(request).then((loaded) => {
					this.setState({ loading: !loaded });
				});
			}
		});
	}

	render() {
		if (this.state.loading) {
			return this.props.children[0]
		}
		return <View/>
	}
}

module.exports = {
	Navigation,
};

export {
	Navigation,
}
