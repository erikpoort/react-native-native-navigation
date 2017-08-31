import React, { Component } from 'react';
import { AppRegistry, BackHandler } from 'react-native';
import ReactNativeNativeNavigation from './../ReactNativeNativeNavigation';

const registerScreen = (screenID, screen) => {
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
		return this.props.children[0]
	}
}

module.exports = {
	registerScreen,
	Navigation,
}
