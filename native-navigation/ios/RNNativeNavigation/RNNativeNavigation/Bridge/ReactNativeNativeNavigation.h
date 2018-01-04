//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>

@class NNBaseNode;

@interface ReactNativeNativeNavigation : NSObject <RCTBridgeModule>
+ (void)addExternalNodes:(NSArray <NNBaseNode*> *)nodes;
@end
