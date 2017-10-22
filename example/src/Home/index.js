import React, { Component } from 'react';
import { View, Text, Button } from 'react-native';
import { SingleView } from 'react-native-native-navigation';
import Detail from '../Detail';

export default class Home extends Component {
	static pageMap = [Detail];
	handleDetail = () => {
		this.props.stack.push(
			<SingleView id="Detail" screen={Detail}/>
		);
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
