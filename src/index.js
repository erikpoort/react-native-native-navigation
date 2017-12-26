import React, { Component } from 'react';
import { 
	DrawerView,
	SingleNavigation,
	SingleView,
	SplitView,
	StackNavigation,
	StackView,
	TabView,
	Navigation
} from '../native-navigation';
import { View, Text } from 'react-native';
import Home from './Home';
import Loading from './Loading';

export default class example extends Component {
	render() {
		return (
			<Navigation pages={[Loading, Home]}>
				<Loading/>
				<StackView id='stack'>
					<SingleView id='home' screen={Home} lazyLoad='true'/>
				</StackView>
			</Navigation>
		);
	}
}
