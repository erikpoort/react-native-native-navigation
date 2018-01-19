import React, { Component } from 'react';
import { SingleView, StackView, SplitView } from '../../native-navigation';
import Detail from '../Detail';

export default class Home extends Component {
	static pageMap = [Detail];

	render() {
		return (
			<StackView id='home_stack'>
				<SingleView id='detail' screen={Detail} lazyLoad='true'/>
				<SplitView id='split' axis={SplitView.AXIS.VERTICAL}>
					<StackView id='up_stack'>
						<SingleView id='detail' screen={Detail} lazyLoad='true'/>
					</StackView>
					<StackView id='bottom_stack'>
						<SingleView id='detail2' screen={Detail} lazyLoad='true'/>
					</StackView>
				</SplitView>
			</StackView>
		);
	}
}
