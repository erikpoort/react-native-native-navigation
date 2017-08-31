//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <React/RCTBridge.h>
#import "NNSingleNode.h"
#import "NNSingleView.h"

static NSString *const kName = @"name";
static NSString *const kScreenId = @"screenID";

@interface NNSingleNode ()

@property (nonatomic, strong) RCTBridge *bridge;
@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *screenID;

@end

@implementation NNSingleNode

+ (NSString *)jsName
{
	return @"SingleView";
}

- (UIViewController *)generate {
	return [[NNSingleView alloc] initWithBridge:self.bridge node:self];
}

- (void)setData:(NSDictionary *)data
{
	self.title = data[kName];
	self.screenID = data[kScreenId];
}

@end
