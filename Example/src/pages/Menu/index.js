import React, { Component } from 'react';
import { Button, processColor, Text, View } from 'react-native';
import Detail from '../Detail';
import Detail2 from "../Detail2";
import { SingleView } from "react-native-native-navigation";

export default class Menu extends Component {
	static pageMap = [Detail];

	handleMenuItem = (view) => {
		this.props.stack.push(view);
	};

	render() {
		return (
			<View>
				<Text>Menu</Text>
				<Button title="Detail" onPress={() => this.handleMenuItem(
					<SingleView id="Detail" screen={Detail} style={{
						barBackground: processColor('#ff0'),
						barTint: processColor('#00f'),
					}}/>
				)}/>
				<Button title="Detail 2" onPress={() => this.handleMenuItem(
					<SingleView id="Detail2" screen={Detail2} style={{
						barBackground: processColor('#f0f'),
						barTint: processColor('#0f0'),
					}}/>
				)}/>
			</View>
		);
	}
}
