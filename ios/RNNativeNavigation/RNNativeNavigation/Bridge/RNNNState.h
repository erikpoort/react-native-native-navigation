//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RNNNState : NSObject

@property (nonatomic, strong) NSDictionary *state;
@property (nonatomic, readonly) UIWindow *window;

+ (instancetype)sharedInstance;

@end
