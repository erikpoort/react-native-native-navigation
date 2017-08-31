//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <React/RCTRootView.h>
#import "NNSingleView.h"
#import "NNSingleNode.h"

@interface NNSingleView ()

@property (nonatomic, strong) RCTBridge *bridge;
@property (nonatomic, copy) NSString *screenID;

@end

@implementation NNSingleView

- (instancetype)initWithBridge:(RCTBridge *)bridge node:(NNSingleNode *)node {
    if (self = [super init]) {
        self.bridge = bridge;
        self.screenID = node.screenID;
        self.title = node.title;
    }
    return self;
}

- (void)loadView {
    self.view = [[RCTRootView alloc] initWithBridge:self.bridge moduleName:self.screenID initialProperties:nil];
}

@end
