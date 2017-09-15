import React, { Component } from 'react';
import { TabView, StackView, SingleView, SplitView, Navigation } from 'react-native-native-navigation';
import Home from './Home';
import Loading from './Loading';
import Detail from './Detail';
import Detail2 from './Detail2';

export default class example extends Component {
  render() {
    return (
        <Navigation pages={[Loading, Home, Detail, Detail2]}>
          <Loading />
            <TabView name="tabs" selectedTab={0}>
              <StackView name="stack">
                <SingleView screen={Home} />
                <SingleView screen={Detail} />
                <SingleView screen={Detail2} />
              </StackView>
              <SingleView screen={Detail2} />
              <SplitView name="horizontal" axis={SplitView.AXIS.HORIZONTAL}>
                <SingleView screen={Detail} />
                <SingleView screen={Detail2} />
              </SplitView>
              <SplitView name="vertical" axis={SplitView.AXIS.VERTICAL}>
                <SingleView screen={Detail} />
                <StackView name="stack">
                  <SingleView screen={Home} />
                </StackView>
              </SplitView>
            </TabView>
        </Navigation>
    );
  }
}
