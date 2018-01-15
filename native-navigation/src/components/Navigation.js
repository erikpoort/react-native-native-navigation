import React, { Component } from 'react';
import { AppRegistry, View } from 'react-native';
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
	getNode = (dom) => Navigation.getNode(dom);

	static mapChild = (dom, path) => {
		const node = Navigation.getNode(dom);
		return node.type.mapToDictionary(node, path);
	};
	mapChild = (dom, path) => Navigation.mapChild(dom, path);

	registerScreen = (screenID, screen) => {
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
	};

	registerScreens = (screens) => {
		screens.forEach((screenData) => {
			const { screenID, screen } = screenData;
			this.registerScreen(screenID, screen)
		});
	};

	generateSiteMap = () => {
		const dom = this.props.children[1];
		return this.mapChild(dom, '');
	};

	generatePageMap = (array, page) => {
		if (page.pageMap) {
			return [
				...array,
				...page.pageMap.reduce((innerArray, innerPage) => {
					return this.generatePageMap(innerArray, innerPage);
				}, []),
				page,
			]
		} else {
			return [
				...array,
				page,
			];
		}
	};

	componentDidMount() {
		this.pageMap = this.props.pages.reduce((array, page) =>
				this.generatePageMap(array, page),
			[]).reduce((map, page) => {
			return {
				...map,
				[page.name]: page,
			}
		}, {});
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
		ReactNativeNativeNavigation.onStart((request) => {
			if (!request) {
				request = this.generateSiteMap();
			}

			if (request) {
				const dom = this.viewMap[request.type];
				const screens = dom.reduceScreens(request, this.viewMap, this.pageMap);
				this.registerScreens(screens);

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

		const { store } = this.props;

		if (this.props.provider && store) {
			return (
				<this.props.provider store={store}>
					<View/>
				</this.props.provider>
			)
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
