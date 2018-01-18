import React, { Component } from 'react';
import { Button, View, Text } from 'react-native';
import Detail from '../Detail';
import Home from "../Home";
import Detail2 from "../Detail2";
import {SingleView,StackView} from "../../../native-navigation/src/index";

export default class Menu extends Component {
	static pageMap = [Detail];
	handleMenuItem = (view) => {
		this.props.drawer.openView('center', view, (callback) => {

		})
	};
	render(){
		return (
				<View>
					<Text>Menu</Text>
					<Button title="Home" onPress={() => this.handleMenuItem(<Home/>)} />
					<Button title="Detail" onPress={() => this.handleMenuItem(<SingleView id="Detail" screen={Detail}/>)} />
					<Button title="Detail 2" onPress={() => this.handleMenuItem(
						<StackView id="stackert">
                            <SingleView id="Detail2" screen={Detail2}/>
                            <SingleView id="Detail2" screen={Detail2}/>
                            <SingleView id="Detail2" screen={Detail2}/>
						</StackView>
					)} />
				</View>
		);
	}
}
