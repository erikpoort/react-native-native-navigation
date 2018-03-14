//
// Created by Juan Curti on 1/4/18.
// Copyright (c) 2018 Facebook. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NNView.h"

@class RCTBridge;
@class ExampleNode;


@interface ExampleView : UIViewController <NNView>

- (instancetype)initWithBridge:(RCTBridge *)bridge node:(ExampleNode *)node;

@end
