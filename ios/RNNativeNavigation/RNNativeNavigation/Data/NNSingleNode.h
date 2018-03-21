//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNBaseNode.h"


@interface NNSingleNode : NNBaseNode <NNNode>

@property (nonatomic, strong) id<NNNode> modal;
@property (nonatomic, copy, readonly) NSDictionary<NSString *, id> *style;

@end
