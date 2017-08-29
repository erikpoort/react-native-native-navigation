//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <React/RCTBridge.h>
#import "NNSingleNode.h"
#import "NNSingleView.h"

static NSString *const kScreenId = @"screenID";

@interface NNSingleNode ()

@property (nonatomic, strong) RCTBridge *bridge;
@property (nonatomic, copy) NSString *screenID;

@end

@implementation NNSingleNode

+ (NSString *)jsName
{
	return @"SingleView";
}

- (void)setData:(NSDictionary *)data
{
	self.screenID = data[kScreenId];
}

- (UIViewController *)generate {
    return [[NNSingleView alloc] initWithBridge:self.bridge screenID:self.screenID];
}

@end
