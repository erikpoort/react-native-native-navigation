import React, { Component } from 'react';
import { StackView, SingleView } from '../../native-navigation';
import Home from '../Home';

export default class Detail2 extends Component {
	render(){
		return (
			<StackView id="stack_up">
				<SingleView id="Home" screen={Home}/>
			</StackView>
		);
	}
}
