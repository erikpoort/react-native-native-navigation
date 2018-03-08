//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridge.h>

@protocol NNView;

@protocol NNNode <NSObject>

+ (NSString *)jsName;

- (void)setBridge:(RCTBridge *)bridge;

- (void)setData:(NSDictionary *)data;

- (NSDictionary *)data;

- (NSString *)title;

- (NSString *)screenID;

- (UIViewController <NNView> *)generate;

+ (NSDictionary<NSString *, id> *)constantsToExport;

@end
