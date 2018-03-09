import React, { Component } from 'react';
import { Button, Text, View } from 'react-native';
import { SingleView } from '../../../native-navigation/src';

export default class Detail2 extends Component {
	render(){
		return (
			<View>
				<Text>Detail</Text>
				<Button title="Detail 2" onPress={() => {
					this.props.up_stack.pop();
				}} />
				<Button title="Modal" onPress={() => {
					this.props.single.showModal(<SingleView id="modal" screen={Detail2} />);
				}} />
			</View>
		);
	}
}
