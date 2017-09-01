//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <React/RCTBridge.h>
#import "NNSingleNode.h"
#import "NNSingleView.h"
#import "NNStackNode.h"

static NSString *const kName = @"name";
static NSString *const kScreenId = @"screenID";
static NSString *const kParentType = @"parentType";

@interface NNSingleNode ()

@property (nonatomic, strong) RCTBridge *bridge;
@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *screenID;
@property (nonatomic, assign) NNParentType parentType;

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
	self.title = data[kName];
	self.screenID = data[kScreenId];

	NSArray *parentTypes = @[NNStackNode.jsName];
	NSUInteger index = [parentTypes indexOfObject:data[kParentType]];
	if (index != NSNotFound) {
		self.parentType = index + 1;
	}
}

- (NSDictionary *)data {
	return @{
			kName: self.title,
			kScreenId:  self.screenID,
	};
}

@end
