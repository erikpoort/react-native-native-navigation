import React, { Component } from 'react';
import { NativeModules, AppRegistry, Text } from 'react-native';
const { ReactNativeNativeNavigation } = NativeModules;

class Navigation extends Component {
	registerScreen = dom => {
		const name = dom.type.name;
		const NavigationComponent = dom.props.screen;
		AppRegistry.registerComponent(name, () => {
			return class extends Component {
				render() {
					return (<NavigationComponent/>)
				}
			}
		});
	}
	mapToDictionary = dom => {
		const name = dom.type.name;
		return {[name]: {}};
	}
	setSiteMap = () => {
		let dom = this.props.children[1];
		let map = this.mapToDictionary(dom);
		this.registerScreen(dom);
		return map;
	}
	componentDidMount() {
		map = this.setSiteMap();
		ReactNativeNativeNavigation.onStart()
			.catch(() => ReactNativeNativeNavigation.setSiteMap(map));
	}
	render() {
		return (this.props.children[0])
	}
}

class SingleView extends Component {
};

export {
	Navigation,
	SingleView
}