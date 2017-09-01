//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NNNode.h"

typedef NS_ENUM(NSUInteger, NNParentType) {
	NNParentTypeUnknown,
	NNParentTypeStack,
};

@interface NNSingleNode : NSObject <NNNode>

@property (nonatomic, copy, readonly) NSString *screenID;
@property (nonatomic, readonly) NNParentType parentType;

@end
