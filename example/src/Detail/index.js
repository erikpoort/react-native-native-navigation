import React, { Component } from 'react';
import { View, Text, Button } from 'react-native';
import { TabView, SingleView } from 'react-native-native-navigation';
import Home from './../Home';
import Detail2 from './../Detail2';

export default class Detail extends Component {
	handleDetail = () => {
		this.props.single.showModal(<TabView id="stack">
			<SingleView id="Home" screen={Home}/>
			<Detail2 />
		</TabView>);
	};
	render(){
		return (
			<View>
				<Text>Detail</Text>
				<Button title="Detail 2" onPress={this.handleDetail} />
			</View>
		);
	}
}
