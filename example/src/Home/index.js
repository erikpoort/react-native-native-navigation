import React, { Component } from 'react';
import { View, Text, Button } from 'react-native';
import Detail from '../Detail';

export default class Home extends Component {
	handleDetail = () => {
		this.props.stack.push(<Detail />);
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
