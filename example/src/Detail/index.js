import React, { Component } from 'react';
import { Button, Text, View } from 'react-native';
import { SingleView } from 'react-native-native-navigation';

export default class Detail extends Component {
	handleDetail = () => {
		this.props.stack.push(
			<SingleView id="Detail" screen={Detail}/>
		);
	};
	render(){
		return (
			<View>
				<Text>Detail</Text>
				<Button title="Detail" onPress={this.handleDetail} />
			</View>
		);
	}
}
