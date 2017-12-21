//

// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol NNNode;
@class RCTBridge;

@interface NNNodeHelper : NSObject

+ (id <NNNode>)nodeFromMap:(NSDictionary *)map bridge:(RCTBridge *)bridge;

@end