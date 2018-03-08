//

// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <React/RCTBridge.h>
#import "RNNNState.h"

@interface RNNNState ()

@property(nonatomic, strong) UIWindow *window;

@end

@implementation RNNNState

+ (instancetype)sharedInstance {
    static dispatch_once_t once;
    static id sharedInstance;
    dispatch_once(&once, ^{
        sharedInstance = [[self alloc] init];
    });
    return sharedInstance;
}

- (instancetype)init {
    if (self = [super init]) {
        [self newWindow];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onReload) name:RCTJavaScriptDidLoadNotification object:nil];
    }
    return self;
}

- (void)onReload {
    [self newWindow];
}

- (void)newWindow {
    if (self.window) {
        [self.window removeFromSuperview];
    }

    self.window = [[UIWindow alloc] initWithFrame:[UIApplication sharedApplication].keyWindow.bounds];
}

@end