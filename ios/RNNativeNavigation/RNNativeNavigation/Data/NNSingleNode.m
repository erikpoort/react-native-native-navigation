//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <React/RCTBridge.h>
#import "NNSingleNode.h"
#import "NNSingleView.h"

@interface NNSingleNode ()

@property (nonatomic, strong) RCTBridge *bridge;
@property (nonatomic, copy) NSString *screenID;

@end

@implementation NNSingleNode {

}

- (instancetype)initWithBridge:(RCTBridge *)bridge screenID:(NSString *)screenID
{
    if (self = [super init]) {
        self.bridge = bridge;
        self.screenID = screenID;
    }
    return self;
}

- (UIViewController *)generate {
    return [[NNSingleView alloc] initWithBridge:self.bridge screenID:self.screenID];
}

@end
