//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridge.h>

@protocol NNView;
@class ReactNativeNativeEventEmitter;

@protocol NNNode <NSObject>

+ (NSString *)jsName;

- (void)setBridge:(RCTBridge *)bridge;

- (void)setData:(NSDictionary *)data;

- (NSDictionary *)data;

- (NSString *)screenID;

- (UIViewController<NNView> *)generate;

- (NSArray<NSString *> *)supportedEvents;

+ (NSDictionary<NSString *, id> *)constantsToExport;

@end
