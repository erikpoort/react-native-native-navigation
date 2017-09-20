import React, { Component } from 'react';
import { View, Text, Button } from 'react-native';
import Detail2 from './../Detail2'

export default class Detail extends Component {
	handleDetail = () => {
		this.props.single.showModal(Detail2);
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
