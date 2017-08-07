//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNStackView.h"
#import "NNNode.h"

@implementation NNStackView {

}

- (instancetype)initWithStack:(NSArray <id <NNNode>> *)stack {
    self = [super init];

    if (self) {
        NSMutableArray *viewControllers = [@[] mutableCopy];
        [stack enumerateObjectsUsingBlock:^(id <NNNode> view, NSUInteger idx, BOOL *stop) {
            [viewControllers addObject:[view generate]];
        }];
        self.viewControllers = [viewControllers copy];
    }

    return self;
}

- (NSString *)title {
    return self.viewControllers.lastObject.title;
}

@end
