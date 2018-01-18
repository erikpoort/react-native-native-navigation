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
import Loading from './pages/Loading';

import { Provider } from 'react-redux';
import store from './redux/store';
import ExampleView from './custom_nodes/ExampleView';

export default class example extends Component {
	constructor() {
		super();

		const navigation = new Navigation(
			[Loading, Home, Menu],
			[ExampleView],
			Provider, store
		);

		navigation.start(
			<TabView id='tab'>
				<DrawerView
					id='drawer'
					left={
						<SplitView id='split' axis={SplitView.AXIS.VERTICAL}>
							<SingleView id='menu' screen={Menu}/>
							<StackView id='stack'>
								<SingleView id='menu' screen={Menu}/>
							</StackView>
						</SplitView>
					}
					center={<SingleView id='menu' screen={Menu}/>}/>
				<SingleView id='menu' screen={Menu}/>
			</TabView>
		);
	}

	render() {
		return <Loading/>
	}
}
