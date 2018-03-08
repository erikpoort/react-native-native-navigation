//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RNNNState : NSObject

@property(nonatomic, readonly) UIWindow *window;

@property(nonatomic, strong) NSDictionary *state;

+ (instancetype)sharedInstance;

@end
