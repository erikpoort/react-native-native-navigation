//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNTabNode.h"
#import "NNTabView.h"

@implementation NNTabNode {
    NSArray <id <NNNode>> *_tabs;
    NSUInteger _selectedTab;
}

- (instancetype)initWithTabs:(NSArray <id <NNNode>> *)tabs selectedTab:(NSUInteger)selectedTab {
    self = [super init];

    if (self) {
        _tabs = tabs;
        _selectedTab = selectedTab;
    }

    return self;
}

- (UIViewController *)generate {
    return [[NNTabView alloc] initWithTabs:_tabs selectedTab:_selectedTab];
}

@end
