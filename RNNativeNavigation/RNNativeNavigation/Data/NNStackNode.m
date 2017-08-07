//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNStackNode.h"
#import "NNStackView.h"

@implementation NNStackNode {

    NSArray <id <NNNode>> *_stack;
}

- (instancetype)initWithStack:(NSArray <id <NNNode>> *)stack {
    self = [super init];

    if (self) {
        _stack = stack;
    }

    return self;
}

- (UIViewController *)generate {
    return [[NNStackView alloc] initWithStack:_stack];
}

@end
