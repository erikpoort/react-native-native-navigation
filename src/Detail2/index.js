import React, { Component } from 'react';
import { Button, Text, View } from 'react-native';

export default class Detail2 extends Component {
	render(){
		return (
			<View>
				<Text>Detail</Text>
				<Button title="Detail 2" onPress={() => alert('pop!')} />
			</View>
		);
	}
}
