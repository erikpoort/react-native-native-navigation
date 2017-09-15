//

// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NNNode.h"

FOUNDATION_EXPORT NSString *const kScreenId;
FOUNDATION_EXPORT NSString *const kType;

@interface NNBaseNode : NSObject

@property (nonatomic, copy, readonly) NSString *screenID;

- (void)setData:(NSDictionary *)data;
- (NSDictionary *)data;

@end