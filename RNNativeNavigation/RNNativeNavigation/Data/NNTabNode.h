//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NNNode.h"

@interface NNTabNode : NSObject <NNNode>

- (instancetype)initWithTabs:(NSArray <id <NNNode>> *)tabs selectedTab:(NSUInteger)selectedTab;

@end
