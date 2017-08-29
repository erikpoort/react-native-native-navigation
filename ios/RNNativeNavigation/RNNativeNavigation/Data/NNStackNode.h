//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NNNode.h"

@interface NNStackNode : NSObject <NNNode>

@property (nonatomic, copy, readonly) NSArray <id <NNNode>> *stack;

@end
