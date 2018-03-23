import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { Button, Text, View, processColor } from 'react-native';
import { changeAxis } from '../../redux/actions/split-actions';
import { SingleView, SplitView } from 'react-native-native-navigation';
import Detail2 from '../Detail2/index';

class Detail extends Component {
	static pageMap = [Detail2];

	render() {
		return (
			<View>
				<Text>Detail {this.props.axis}</Text>
				<Button
					title="Vertical"
					onPress={() => this.props.changeAxis(SplitView.AXIS.VERTICAL)}
				/>
				<Button
					title="Horizontal"
					onPress={() => this.props.changeAxis(SplitView.AXIS.HORIZONTAL)}
				/>
				<Button
					title="Replace"
					onPress={() => this.props.navigation.start(
						<SingleView id="Detail2" screen={Detail2} />,
						true
					)}
				/>
				<Button
					title="Tab 1"
					onPress={() => this.props.tabs.openTab(0)}
				/>
				<Button
					title="Change title"
					onPress={() => this.props.single.updateStyle({
						title: "Changed",
						barTint: processColor("#00f"),
						barBackground: processColor('#f0f'),
					})}
				/>
			</View>
		);
	}
}

mapStateToProps = (state) => {
	return {
		axis: state.split.axis
	}
};

mapDispatchToProps = (dispatch) => bindActionCreators({
	changeAxis: (axis) => changeAxis(axis),
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Detail);
