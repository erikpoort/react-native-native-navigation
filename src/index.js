import React, { Component } from 'react';
import { NativeModules, AppRegistry, Text } from 'react-native';
const { ReactNativeNativeNavigation } = NativeModules;

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

class Navigation extends Component {
	setSiteMap = () => {
		let dom = this.props.children[1];
		return dom.type.mapToDictionary(dom);
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
	static mapToDictionary = dom => {
		const name = dom.type.name;
		const screenID = name;

		this.registerScreen(dom);

		return {
			name,
			screenID,
		};
	}
};
class StackView extends Component {
	static mapChildren = children => {
		if (!Array.isArray(children)) children = [children];
		return children.map(dom => dom.type.mapToDictionary(dom));
	}
	static mapToDictionary = dom => {
		const name = dom.type.name;
		const stack = dom.type.mapChildren(dom.props.children);
		return {
			name,
			stack,
		};
	}
};

export {
	Navigation,
	SingleView,
	StackView
}