import React, { Component } from 'react';
import { View, Text, Button } from 'react-native';
import {
	TabView,
	SingleView,
} from 'react-native-native-navigation';

import Detail from '../Detail';
import Detail2 from '../Detail2';

export default class Home extends Component {
	handleDetail = () => {
		this.props.stack.push(Detail);
	};
	render(){
		return (
			<TabView name="tabs" selectedTab={0}>
				<SingleView screen={Detail} name="Detail"/>
				<SingleView screen={Detail2} name="Detail2"/>
			</TabView>
		);
	}
}
