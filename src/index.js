import React, { Component } from 'react';
import { NativeModules, AppRegistry, BackHandler } from 'react-native';
const { ReactNativeNativeNavigation } = NativeModules;

registerScreen = (screenID, screen) => {
	AppRegistry.registerComponent(screenID, () => {
		const Screen = screen;
		return class extends Component {
			removeBackButtonListener;
			componentWillMount() {
				const { remove } = BackHandler.addEventListener('hardwareBackPress', function() {
					ReactNativeNativeNavigation.handleBackButton(handled => {
						if (!handled) {
							BackHandler.exitApp();
						}
					})
					return true;
				});
				this.removeBackButtonListener = remove;
			}
			componentWillUnmount() {
				if (this.removeBackButtonListener != null) {
					this.removeBackButtonListener();
				}
			}
			render() {
				return (<Screen/>)
			}
		}
	});
}

class Navigation extends Component {
	setSiteMap = () => {
		let dom = this.props.children[1];
		return dom.type.mapToDictionary('', dom);
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
	static mapToDictionary = (path, dom) => {
		const { screen } = dom.props;
		const name = screen.name;
		const type = dom.type.name;
		const screenID = `${path}/${name}`;
		this.registerScreen(screenID, screen);
		return {
			name,
			type,
			screenID,
		};
	}
};
class StackView extends Component {
	static mapChildren = (path, children) => {
		if (!Array.isArray(children)) children = [children];
		return children.map(dom => dom.type.mapToDictionary(path, dom));
	}
	static mapToDictionary = (path, dom) => {
		const type = dom.type.name;
		const stack = dom.type.mapChildren(path, dom.props.children);
		return {
			type,
			stack,
		};
	}
};

export {
	Navigation,
	SingleView,
	StackView
}