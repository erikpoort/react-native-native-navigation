//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNBaseNode.h"
#import "NNStackNode.h"
#import "NNTabNode.h"

NSString *const kScreenId = @"screenID";
NSString *const kParentType = @"parentType";
NSString *const kType = @"type";

@interface NNBaseNode ()

@property (nonatomic, copy) NSString *screenID;
@property (nonatomic, assign) NNParentType parentType;

@end

@implementation NNBaseNode

- (void)setData:(NSDictionary *)data
{
	self.screenID = data[kScreenId];

	NSArray *parentTypes = @[NNStackNode.jsName, NNTabNode.jsName];
	NSUInteger index = [parentTypes indexOfObject:data[kParentType]];
	if (index != NSNotFound) {
		self.parentType = (NNParentType)(index + 1);
	}
}

- (NSDictionary *)data
{
	return @{
			kScreenId: self.screenID,
			kParentType: @(self.parentType),
	};
}

@end