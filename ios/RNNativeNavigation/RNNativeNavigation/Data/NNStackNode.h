//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NNNode.h"

@interface NNStackNode : NSObject <NNNode>

- (instancetype)initWithStack:(NSArray <id <NNNode>> *)stack;

@end
