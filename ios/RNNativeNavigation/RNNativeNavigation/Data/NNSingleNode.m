//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <React/RCTBridge.h>
#import "NNSingleNode.h"
#import "NNSingleView.h"

static NSString *const kName = @"name";

@interface NNSingleNode ()

@property (nonatomic, strong) RCTBridge *bridge;
@property (nonatomic, copy) NSString *title;

@end

@implementation NNSingleNode

+ (NSString *)jsName
{
	return @"SingleView";
}

- (UIViewController *)generate {
	return [[NNSingleView alloc] initWithBridge:self.bridge node:self];
}

- (void)setData:(NSDictionary *)data {
	[super setData:data];
	self.title = data[kName];
}

- (NSDictionary *)data {
	NSMutableDictionary *data = [super data].mutableCopy;
	data[kName] = self.title;
	data[kType] = self.class.jsName;
	return data.copy;
}

@end
