//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNBaseNode.h"

NSString *const kScreenId = @"screenID";
NSString *const kType = @"type";

@interface NNBaseNode ()

@property (nonatomic, copy) NSString *screenID;

@end

@implementation NNBaseNode

- (void)setData:(NSDictionary *)data
{
	self.screenID = data[kScreenId];
}

- (NSDictionary *)data
{
	return @{
			kScreenId: self.screenID,
	};
}

@end