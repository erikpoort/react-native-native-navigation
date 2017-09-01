import React, { Component } from 'react';
import { StackView, SingleView, Navigation } from 'react-native-native-navigation';
import Home from './Home';
import Loading from './Loading';
import Detail from './Detail';

export default class example extends Component {
  render() {
    return (
        <Navigation pages={[Loading, Home, Detail]}>
          <Loading />
          <StackView>
            <SingleView screen={Home} />
          </StackView>
        </Navigation>
    );
  }
}
