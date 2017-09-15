import React, { Component } from 'react';
import { AppRegistry, View } from 'react-native';
import ReactNativeNativeNavigation from './../ReactNativeNativeNavigation';
import SingleView from './single/SingleView';
import StackView from './stack/StackView';
import TabView from './tab/TabView';
import SplitView from './split/SplitView';

class Navigation extends Component {
	static pageMap;
	static viewMap;

	state = {
		loading: true,
	};

	static registerScreen = (screenID, screen) => {
		const Screen = screen;
		AppRegistry.registerComponent(screenID, () => {
			return class extends Component {
				render() {
					return (
						<Screen />
					)
				}
			}
		});
	}
	registerScreens = (screens) => {
		screens.forEach((screenData) => {
			const { screenID, screen } = screenData;
			Navigation.registerScreen(screenID, screen)
		});
	}

	generateSiteMap = () => {
		const dom = this.props.children[1];
		return dom.type.mapToDictionary(dom, '');
	}

	componentDidMount() {
		const pageMap = this.props.pages.reduce((map, page) => {
			return {
				...map,
				[page.name]: page
			}
		}, {});
		Navigation.pageMap = pageMap;
		const viewMap = {
			[SingleView.name] : SingleView,
			[StackView.name] : StackView,
			[TabView.name] : TabView,
			[SplitView.name] : SplitView,
		};
		Navigation.viewMap = viewMap;

		ReactNativeNativeNavigation.onStart((request) => {
			if (request == null) {
				request = this.generateSiteMap();
			}

			const dom = Navigation.viewMap[request.type];
			const screens = dom.reduceScreens(request, Navigation.viewMap, Navigation.pageMap);
			this.registerScreens(screens);

			ReactNativeNativeNavigation.setSiteMap(request).then((loaded) => {
				this.setState({loading: !loaded});
			});
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
}

export {
	Navigation,
}
