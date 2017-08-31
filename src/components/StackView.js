import React, { Component } from 'react';

export default class StackView extends Component {
	static mapChildren = (path, children) => {
		if (!Array.isArray(children)) children = [children];
		return children.map(dom => dom.type.mapToDictionary(path, dom));
	}
	static mapToDictionary = (path, dom) => {
		const type = dom.type.name;
		const stack = dom.type.mapChildren(path, dom.props.children);
		return {
			type,
			stack,
		};
	}
}
