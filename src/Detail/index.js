import React, { Component } from 'react';
import { Button, Text, View } from 'react-native';
import { SingleView } from '../../native-navigation';
import Detail2 from '../Detail2';

export default class Detail extends Component {
	static pageMap = [Detail2];
	handleDetail = (name) => {
		this.props[name].push(
			<SingleView id="Detail2" screen={Detail2}/>
		);
	};
	render(){
		return (
			<View>
				<Text>Detail</Text>
				<Button title="Detail" onPress={()=>this.handleDetail('stack')} />
				<Button title="Detail" onPress={()=>this.handleDetail('home_stack')} />
				<Button title="Detail" onPress={()=>this.handleDetail('two_stack')} />
			</View>
		);
	}
}
