import React, { Component } from 'react';
import { SingleView, Navigation } from 'react-native-native-navigation';
import Home from './Home';

export default class example extends Component {
  render() {
    return (
        <Navigation>
          <SingleView screen={Home} />
        </Navigation>
    );
  }
}
