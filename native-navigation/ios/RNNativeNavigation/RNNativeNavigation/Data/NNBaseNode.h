//

// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NNNode.h"

@interface NNBaseNode : NSObject

@property (nonatomic, copy, readonly) NSString *screenID;

- (void)setData:(NSDictionary *)data;
- (NSDictionary *)data;

@end