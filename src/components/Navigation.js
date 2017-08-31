import React, { Component } from 'react';
import { AppRegistry } from 'react-native';
import ReactNativeNativeNavigation from './../ReactNativeNativeNavigation';

const registerScreen = (navigator, screenID, screen) => {
	AppRegistry.registerComponent(screenID, () => {
		const Screen = screen;
		const Navigator = navigator;
		return class extends Component {
			render() {
				const props = this.props;
				return (
					<Navigator>
						<Screen />
					</Navigator>
				)
			}
		}
	});
}

class Navigation extends Component {
	setSiteMap = () => {
		let dom = this.props.children[1];
		return dom.type.mapToDictionary(dom.type, '', dom);
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
