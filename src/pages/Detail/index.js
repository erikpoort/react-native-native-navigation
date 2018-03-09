import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { Button, Text, View } from 'react-native';
import { changeAxis } from '../../redux/actions/split-actions';
import { SplitView, SingleView } from '../../../native-navigation/src/index';
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
					title="Push"
					onPress={() => this.props.stack.push(<SingleView id="ja" screen={Detail2} />)}
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