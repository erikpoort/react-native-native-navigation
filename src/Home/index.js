import React, { Component } from 'react';
import { SingleView, StackView } from '../../native-navigation';
import Detail from '../Detail';

export default class Home extends Component {
	static pageMap = [Detail];
	render(){
		return (
			<StackView id='home_stack'>
				<SingleView id='detail' screen={Detail} lazyLoad='true'/>
			</StackView>
		);
	}
}
