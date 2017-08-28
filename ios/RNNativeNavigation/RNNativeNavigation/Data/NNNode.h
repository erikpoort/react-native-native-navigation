//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol NNNode <NSObject>

- (instancetype)initWithBridge:(RCTBridge *)bridge screenID:(NSString *)screenID;
- (UIViewController *)generate;

@end
