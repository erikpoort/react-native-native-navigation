//

// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NNNode.h"

FOUNDATION_EXPORT NSString *const kScreenId;
FOUNDATION_EXPORT NSString *const kParentType;
FOUNDATION_EXPORT NSString *const kType;

typedef NS_ENUM(NSUInteger, NNParentType) {
	NNParentTypeUnknown,
	NNParentTypeStack,
	NNParentTypeTab,
};

@interface NNBaseNode : NSObject

@property (nonatomic, copy, readonly) NSString *screenID;
@property (nonatomic, readonly) NNParentType parentType;

- (void)setData:(NSDictionary *)data;
- (NSDictionary *)data;

@end