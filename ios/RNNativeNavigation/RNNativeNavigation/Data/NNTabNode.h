//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NNNode.h"
#import "NNBaseNode.h"


@interface NNTabNode : NNBaseNode <NNNode>

@property (nonatomic, copy) NSArray<id<NNNode>> *tabs;
@property (nonatomic, assign) NSUInteger selectedTab;

@end
