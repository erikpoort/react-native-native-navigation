import React, { Component } from 'react';
import { SingleView, Navigation } from 'react-native-native-navigation';
import Home from './Home';
import Loading from './Loading';

export default class example extends Component {
  render() {
    return (
        <Navigation>
          <Loading />
          <SingleView screen={Home} />
        </Navigation>
    );
  }
}
