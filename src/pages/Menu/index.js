import React, { Component } from 'react';
import { Button, View, Text } from 'react-native';
import Detail from '../Detail';
import Home from "../Home";
import Detail2 from "../Detail2";
import { SingleView, StackView } from "../../../native-navigation/src/index";

export default class Menu extends Component {
	static pageMap = [Detail];
	handleMenuItem = (view) => {
		let path = this.props.single.screenID;
		var comps = path.split("/");
		comps.pop();
		comps.pop();
		let newPath = comps.join("/")
		this.props.stack.popTo(newPath);
	};

	render() {
		return (
			<View>
				<Text>Menu</Text>
				<Button title="Detail" onPress={() => this.handleMenuItem(
					<SingleView id="Detail" screen={Detail}/>
				)}/>
				<Button title="Detail 2" onPress={() => this.handleMenuItem(
					<SingleView id="Detail2" screen={Detail2}/>
				)}/>
				<Button title="Add" onPress={() => {
					this.props.tabs.insertTab(1, <SingleView id="new" screen={Detail} />, true)
				}}/>
				<Button title="Remove" onPress={() => {
					this.props.tabs.removeTab(1, true)
				}}/>
				<Button title="Replace" onPress={() => {
					this.props.tabs.removeTab(2, false)
					this.props.tabs.insertTab(2, <SingleView id="new" screen={Detail} />, false)
				}}/>
			</View>
		);
	}
}
