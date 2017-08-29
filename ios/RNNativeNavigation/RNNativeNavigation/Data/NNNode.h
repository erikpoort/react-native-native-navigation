//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridge.h>

@protocol NNNode <NSObject>

+ (NSString *)jsName;
- (void)setBridge:(RCTBridge *)bridge;
- (void)setData:(NSDictionary *)data;
- (UIViewController *)generate;

@end
