//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <React/RCTUtils.h>
#import "ReactNativeNativeNavigation.h"
#import "RNNNState.h"
#import "NNSingleNode.h"
#import "NNNodeHelper.h"

static NSString *const kRNNN = @"RNNNN";

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
		reject(kRNNN, message, RCTErrorWithMessage(message));
	}
}

RCT_EXPORT_METHOD(setSiteMap:(NSDictionary *)map
			resolver:(RCTPromiseResolveBlock)resolve
			rejecter:(RCTPromiseRejectBlock)reject) {

	[RNNNState sharedInstance].state = map;

	id <NNNode> nodeObject = [NNNodeHelper nodeFromMap:map bridge:self.bridge];
	UIViewController *viewController = [nodeObject generate];

	dispatch_async(dispatch_get_main_queue(), ^{
		UIWindow *window = [RNNNState sharedInstance].window;
		window.rootViewController = viewController;
		[window makeKeyAndVisible];
	});

	resolve(@[]);
}

@end
