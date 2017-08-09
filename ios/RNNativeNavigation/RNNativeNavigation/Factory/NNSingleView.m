//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNSingleView.h"

@implementation NNSingleView {

}

- (instancetype)init {
    self = [super init];
    if (self) {
        self.title = @"Test";
    }

    return self;
}

- (void)loadView {
    [super loadView];
    NSLog(@"Load view");
}

@end
