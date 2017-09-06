//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NNNode.h"
#import "NNBaseNode.h"

@interface NNTabNode : NNBaseNode <NNNode>

@property (nonatomic, copy, readonly) NSArray <id <NNNode>> *tabs;
@property (nonatomic, readonly) NSUInteger selectedTab;

@end
