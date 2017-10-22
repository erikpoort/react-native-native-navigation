import React, { Component } from 'react';
import { Navigation, SingleView, StackView } from 'react-native-native-navigation';
import Home from './Home';
import Loading from './Loading';

export default class example extends Component {
	render() {
		return (
			<Navigation pages={[Loading, Home]}>
				<Loading/>
				<StackView id='stack'>
					<SingleView id='home' screen={Home} />
				</StackView>
			</Navigation>
		);
	}
}
