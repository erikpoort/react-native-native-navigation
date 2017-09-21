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
				<DrawerView
					name="drawer" side="center"
					center={
						<StackView name="stack">
							<SingleView name="Home" screen={Home}/>
							<DrawerView
								name="drawer" side="center"
								left={
									<SingleView name="Detail" screen={Detail}/>
								}
								center={
									<TabView name="tabs" selectedTab={0}>
										<StackView name="stack">
											<SingleView name="Home" screen={Home}/>
											<SingleView name="Detail" screen={Detail}/>
										</StackView>
										<SingleView name="Detail2" screen={Detail2}/>
										<SplitView name="horizontal" axis={SplitView.AXIS.HORIZONTAL}>
											<SingleView name="Detail" screen={Detail}/>
											<SingleView name="Detail" screen={Detail}/>
										</SplitView>
										<SplitView name="vertical" axis={SplitView.AXIS.VERTICAL}>
											<StackView name="stack_up">
												<SingleView name="Home" screen={Home}/>
											</StackView>
											<StackView name="stack_down">
												<SingleView name="Detail" screen={Detail}/>
											</StackView>
										</SplitView>
									</TabView>}
							/>
						</StackView>}
					right={
						<SingleView name="Detail" screen={Detail}/>
					}
				/>
			</Navigation>
		);
	}
}
