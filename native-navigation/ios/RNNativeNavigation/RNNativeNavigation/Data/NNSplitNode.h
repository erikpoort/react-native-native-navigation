//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NNNode.h"
#import "NNBaseNode.h"


@interface NNSplitNode : NNBaseNode <NNNode>

@property (nonatomic, strong) id<NNNode> node1;
@property (nonatomic, strong) id<NNNode> node2;
@property (nonatomic, assign, readonly) UILayoutConstraintAxis axis;

@end
