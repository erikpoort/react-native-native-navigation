import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { Button, Text, View } from 'react-native';
import { changeAxis } from '../../redux/actions/split-actions';
import { SingleView, SplitView } from '../../../native-navigation/src/index';
import Detail2 from '../Detail2/index';

class Detail extends Component {
	static pageMap = [Detail2];
	handleDetail = (name) => {
		this.props[name].push(
			<SingleView id="Detail2" screen={Detail2}/>
		);
	};

	render() {
		return (
			<View>
				<Text>Detail</Text>
				<Button title="Up" onPress={() => this.handleDetail('up_stack')}/>
				<Button title="Bottom" onPress={() => this.handleDetail('bottom_stack')}/>
				<Button title="Top" onPress={() => this.handleDetail('home_stack')}/>
				<Button title="Vertical" onPress={() => this.props.changeAxis(SplitView.AXIS.VERTICAL)}/>
				<Button title="Horizontal"
				        onPress={() => this.props.changeAxis(SplitView.AXIS.HORIZONTAL)}/>
			</View>
		);
	}
}

mapDispatchToProps = (dispatch) => bindActionCreators({
	changeAxis: (axis) => changeAxis(axis),
}, dispatch);

export default connect(null, mapDispatchToProps)(Detail);