import React, { Component } from 'react';
import {
	Navigation,
	DrawerView,
	SingleView,
	SplitView,
	StackView,
	TabView
} from '../node_modules/react-native-native-navigation';
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
			[Home, Menu, Detail],
			[ExampleNode],
			Provider, store
		);

		navigation.start(
			<TabView id='split'>
				<SingleView id='menu' screen={Menu}/>
				<SingleView id={"new" + 1} screen={Detail} />
				<SingleView id={"new" + 2} screen={Detail2} />
			</TabView>
		);
	}

	render() {
		return <Loading/>
	}
}
