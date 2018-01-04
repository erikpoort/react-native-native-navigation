import React, { Component } from 'react';
import { DrawerView, Navigation, SingleView } from '../native-navigation';
import Home from './Home';
import Menu from './Menu';
import Loading from './Loading';

import { Provider } from 'react-redux';
import store from './store';

export default class example extends Component {
	render() {
		return (
			<Navigation pages={[Loading, Home, Menu]} provider={Provider} store={store}>
				<Loading/>
				<DrawerView
					id='stack'
					left={<SingleView id='menu' screen={Menu} lazyLoad='true'/>}
					center={<Home />}
				/>
			</Navigation>
		);
	}
}
