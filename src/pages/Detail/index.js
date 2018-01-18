import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { Button, Text, View } from 'react-native';
import { SingleView } from '../../../native-navigation/src/index';
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
				<Button title={this.props.label} onPress={this.props.updateButton}/>
			</View>
		);
	}
}

mapStateToProps = (state) => {
	return {
		label: state.label,
	};
};

mapDispatchToProps = (dispatch) => bindActionCreators({
		updateButton: () => dispatch({
			type: 'updateButton'
		})
	},
	dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Detail);