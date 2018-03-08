//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol NNNode;

@class RCTBridge;
@class NNBaseNode;

@interface NNNodeHelper : NSObject

+ (instancetype)sharedInstance;

- (void)addExternalNodes:(NSArray <NNBaseNode *> *)nodes;

- (id <NNNode>)nodeFromMap:(NSDictionary *)map bridge:(RCTBridge *)bridge;

- (NSDictionary<NSString *, id> *)constantsToExport;

@end