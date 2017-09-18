import React, { Component } from 'react';
import {
	TabView,
	StackView,
	SingleView,
	SplitView,
	DrawerView,
	Navigation
} from 'react-native-native-navigation';
import Home from './Home';
import Loading from './Loading';
import Detail from './Detail';
import Detail2 from './Detail2';

export default class example extends Component {
	render() {
		return (
			<Navigation pages={[Loading, Home, Detail, Detail2]}>
				<Loading/>
				<DrawerView name="drawer" side="center"
				            left={
					            <SingleView screen={Detail2}/>
				            }
				            center={
					            <TabView name="tabs" selectedTab={0}>
						            <StackView name="stack">
							            <SingleView screen={Home}/>
							            <SingleView screen={Detail}/>
							            <SingleView screen={Detail2}/>
						            </StackView>
						            <SingleView screen={Detail2}/>
						            <SplitView name="horizontal" axis={SplitView.AXIS.HORIZONTAL}>
							            <SingleView screen={Detail}/>
							            <SingleView screen={Detail2}/>
						            </SplitView>
						            <SplitView name="vertical" axis={SplitView.AXIS.VERTICAL}>
							            <StackView name="stack_up">
								            <SingleView screen={Home}/>
							            </StackView>
							            <StackView name="stack_down">
								            <SingleView screen={Detail}/>
							            </StackView>
						            </SplitView>
					            </TabView>}
				            right={
					            <SingleView screen={Detail}/>
				            }
				/>
			</Navigation>
		);
	}
}
