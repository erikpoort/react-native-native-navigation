//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <React/RCTRootView.h>
#import "NNSingleView.h"
#import "NNSingleNode.h"

@interface NNSingleView ()

@property (nonatomic, strong) NNSingleNode *node;
@property (nonatomic, strong) RCTBridge *bridge;

@end

@implementation NNSingleView

- (instancetype)initWithBridge:(RCTBridge *)bridge node:(NNSingleNode *)node {
    if (self = [super init]) {
        self.node = node;
        self.bridge = bridge;
        self.title = node.title;
    }
    return self;
}

- (void)loadView {
    self.view = [[RCTRootView alloc] initWithBridge:self.bridge moduleName:self.node.screenID initialProperties:nil];
}

- (UIViewController <NNView> *)viewForPath:(NSString *)path
{
	return nil;
}

@end
