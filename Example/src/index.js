import React, { Component } from 'react';
import { processColor } from 'react-native';
import {
	Navigation,
	DrawerView,
	SingleView,
	SplitView,
	StackView,
	TabView
} from 'react-native-native-navigation';
import Home from './pages/Home';
import Menu from './pages/Menu';
import Detail from './pages/Detail'
import Detail2 from './pages/Detail2'
import Loading from './pages/Loading';

import { Provider } from 'react-redux';
import store from './redux/store';
import ExampleView, { ExampleNode } from './custom_nodes/ExampleView';

export default class example extends Component {
	constructor() {
		super();

		this.home = new Home();
		const navigation = new Navigation(
			[Menu, Detail],
			[ExampleNode],
			Provider, store
		);

		navigation.start(
			<StackView id='split'>
				<SingleView id='menu' screen={Menu} style={{
					title: 'Menu',
					barTransparent: true,
					barBackground: processColor('#ff0000ff'),
				}} />
			</StackView>
		);
	}

	render() {
		return <Loading/>
	}
}
