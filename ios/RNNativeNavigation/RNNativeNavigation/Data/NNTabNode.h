//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NNNode.h"

@interface NNTabNode : NSObject <NNNode>

@property (nonatomic, copy, readonly) NSArray <id <NNNode>> *tabs;
@property (nonatomic, readonly) NSUInteger selectedTab;

@end
