import React, { Component } from 'react';
import {
	Navigation,
	DrawerView,
	SingleView,
	SplitView,
	StackView,
	TabView
} from '../native-navigation';
import Home from './pages/Home';
import Menu from './pages/Menu';
import Detail from './pages/Detail'
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
			<SplitView id='split' axis={SplitView.AXIS.VERTICAL}>
				<SingleView id='menu' screen={Menu}/>
				<SingleView id='detail' screen={Detail}/>
			</SplitView>
		);
	}

	render() {
		return <Loading/>
	}
}
