//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNBaseNode.h"

@interface NNSingleNode : NNBaseNode <NNNode>

@property (nonatomic, strong, readonly) id <NNNode> modal;

@end
