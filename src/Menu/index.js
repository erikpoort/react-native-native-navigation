import React, { Component } from 'react';
import { Button, View, Text } from 'react-native';
import Detail from '../Detail';
import Home from "../Home";
import Detail2 from "../Detail2";
import {SingleView} from "../../native-navigation/src";

export default class Menu extends Component {
	static pageMap = [Detail];
	handleMenuItem = (view) => {
        this.props.drawer.sayHi()
	};
	render(){
		return (
				<View>
					<Text>Menu</Text>
					<Button title="Home" onPress={() => this.handleMenuItem(<Home />)} />
					<Button title="Detail" onPress={() => this.handleMenuItem(<Detail/>)} />
					<Button title="Detail 2" onPress={() => this.handleMenuItem(<SingleView id="Detail2" screen={Detail2}/>)} />
				</View>
		);
	}
}
