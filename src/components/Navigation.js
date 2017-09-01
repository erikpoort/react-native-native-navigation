import React, { Component } from 'react';
import ReactNativeNativeNavigation from './../ReactNativeNativeNavigation';
import SingleView from './SingleView';
import StackView from './stack/StackView';

class Navigation extends Component {
	setSiteMap = () => {
		const dom = this.props.children[1];
		return dom.type.mapToDictionary(dom.type, '', dom);
	}
	componentDidMount() {
		map = this.setSiteMap();
		const pageMap = this.props.pages.reduce((map, page) => {
			return {
				...map,
				[page.name]: page
			}
		}, {});
		ReactNativeNativeNavigation.onStart((request) => {
			if (request) {
				const dom = this.props.children[1];
				const viewMap = {
					[SingleView.name] : SingleView,
					[StackView.name] : StackView,
				}
				dom.type.handleMap(request, viewMap, pageMap);
				ReactNativeNativeNavigation.setSiteMap(request);
			} else {
				ReactNativeNativeNavigation.setSiteMap(map);
			}
		});
	}
	render() {
		return this.props.children[0]
	}
}

module.exports = {
	Navigation,
}
