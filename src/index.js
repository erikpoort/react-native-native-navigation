import React, { Component } from 'react';

class Navigation extends Component {
	render() {
		return (this.props.children)
	}
}

class SingleView extends Component {
	render() {
		const Screen = this.props.screen;
		return (<Screen />)
	}
};

export {
	Navigation,
	SingleView
}