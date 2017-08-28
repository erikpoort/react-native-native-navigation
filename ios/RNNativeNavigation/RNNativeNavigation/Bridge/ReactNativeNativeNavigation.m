//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <React/RCTUtils.h>
#import "ReactNativeNativeNavigation.h"
#import "RNNNState.h"
#import "NNSingleNode.h"

static NSString *const kRNNNN = @"RNNNN";

@implementation ReactNativeNativeNavigation
RCT_EXPORT_MODULE();

@synthesize bridge = _bridge;

- (instancetype)init {
	if (self = [super init]) {
		[RNNNState sharedInstance];
	}
	return self;
}

RCT_EXPORT_METHOD(onStart:(RCTPromiseResolveBlock)resolve
			rejecter:(RCTPromiseRejectBlock)reject) {

	NSDictionary *state = [RNNNState sharedInstance].state;
	if (state) {
		[self setSiteMap:state resolver:resolve rejecter:reject];
	} else {
		NSLog(@"First load");
		NSString *message = @"A site map is needed to build the views, call setSiteMap";
		reject(kRNNNN, message, RCTErrorWithMessage(message));
	}
}

RCT_EXPORT_METHOD(setSiteMap:(NSDictionary *)map
			resolver:(RCTPromiseResolveBlock)resolve
			rejecter:(RCTPromiseRejectBlock)reject) {

	__block UIViewController *viewController;

	[RNNNState sharedInstance].state = map;

	[map.allKeys enumerateObjectsUsingBlock:^(NSString *key, NSUInteger idx, BOOL *stop)
	{
		if ([key isEqualToString:@"SingleView"]) {
			NNSingleNode *node = [[NNSingleNode alloc] initWithBridge:self.bridge screenID:key];
			viewController = [node generate];
		}
	}];

	dispatch_async(dispatch_get_main_queue(), ^{
		UIWindow *window = [RNNNState sharedInstance].window;
		window.rootViewController = viewController;
		[window makeKeyAndVisible];
	});

	resolve(@[]);
}

@end
