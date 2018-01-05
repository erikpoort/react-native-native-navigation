import React, { Component } from 'react';
import { Button, View, Text } from 'react-native';
import Detail from '../Detail';

export default class Menu extends Component {
	static pageMap = [Detail];
	handleMenuItem = (index) => {
		alert('menu item ' + index);
	};
	render(){
		return (
				<View>
					<Text>Menu</Text>
					<Button title="Home" onPress={() => this.handleMenuItem(0)} />
					<Button title="Detail" onPress={() => this.handleMenuItem(1)} />
					<Button title="Detail 2" onPress={() => this.handleMenuItem(2)} />
				</View>
		);
	}
}
