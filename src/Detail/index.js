import React, { Component } from 'react';
import { Button, Text, View } from 'react-native';
import { SingleView } from '../../native-navigation';
import Detail2 from '../Detail2';

export default class Detail extends Component {
	static pageMap = [Detail2];
	handleDetail = () => {
		this.props.single.showModal(
			<SingleView id="Detail2" screen={Detail2}/>
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
