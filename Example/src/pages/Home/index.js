import React from 'react';
import { SingleView, StackView, SplitView } from 'react-native-native-navigation';
import Detail from '../Detail';

class Home {
	static pageMap = [Detail];

	siteMap() {
		return (
			<StackView id='home_stack'>
				<SingleView id='detail' screen={Detail} />
				<SplitView id='split' axis={SplitView.AXIS.VERTICAL}>
					<StackView id='up_stack'>
						<SingleView id='detail' screen={Detail} />
					</StackView>
					<StackView id='bottom_stack'>
						<SingleView id='detail' screen={Detail} />
					</StackView>
				</SplitView>
			</StackView>
		);
	}
}

export default Home;
