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
					id="drawer" side="center"
					center={
						<StackView id="stack">
							<SingleView id="Home" screen={Home}/>
							<DrawerView
								id="drawer" side="center"
								left={
									<SingleView id="Detail" screen={Detail}/>
								}
								center={
									<TabView id="tabs" selectedTab={0}>
										<StackView id="stack">
											<SingleView id="Home" screen={Home}/>
											<SingleView id="Detail" screen={Detail}/>
										</StackView>
										<SingleView id="Detail2" screen={Detail2}/>
										<SplitView id="horizontal" axis={SplitView.AXIS.HORIZONTAL}>
											<SingleView id="Detail" screen={Detail}/>
											<SingleView id="Detail" screen={Detail}/>
										</SplitView>
										<SplitView id="vertical" axis={SplitView.AXIS.VERTICAL}>
											<StackView id="stack_up">
												<SingleView id="Home" screen={Home}/>
											</StackView>
											<StackView id="stack_down">
												<SingleView id="Detail" screen={Detail}/>
											</StackView>
										</SplitView>
									</TabView>}
							/>
						</StackView>}
					right={
						<SingleView id="Detail" screen={Detail}/>
					}
				/>
			</Navigation>
		);
	}
}
