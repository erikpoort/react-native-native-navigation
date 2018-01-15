//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NNBaseNode.h"
#import "NNNode.h"
#import "MMDrawerController.h"

@interface NNDrawerNode : NNBaseNode <NNNode>

@property (nonatomic, strong) id <NNNode> leftNode;
@property (nonatomic, strong) id <NNNode> centerNode;
@property (nonatomic, strong) id <NNNode> rightNode;
@property (nonatomic, assign) MMDrawerSide side;

@end
