import React, { Component } from 'react';
import { View, Text, Button } from 'react-native';
import { TabView, SingleView } from 'react-native-native-navigation';
import Detail from '../Detail';

export default class Home extends Component {
	handleDetail = () => {
		this.props.stack.push(<TabView id="stack">
			<SingleView id="Home" screen={Home}/>
			<SingleView id="Detail" screen={Detail}/>
		</TabView>);
	};
	render(){
		return (
			<View>
				<Text>Home</Text>
				<Button title="Detail" onPress={this.handleDetail} />
			</View>
		);
	}
}
