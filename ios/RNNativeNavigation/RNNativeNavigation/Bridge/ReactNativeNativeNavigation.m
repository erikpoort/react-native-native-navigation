//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <React/RCTUtils.h>
#import "ReactNativeNativeNavigation.h"
#import "RNNNState.h"
#import "NNSingleNode.h"
#import "NNNodeHelper.h"
#import "NNStackNode.h"

static NSString *const kRNNN = @"RNNN";

@implementation ReactNativeNativeNavigation
RCT_EXPORT_MODULE();

@synthesize bridge = _bridge;

- (instancetype)init {
	if (self = [super init]) {
		[RNNNState sharedInstance];
	}
	return self;
}

RCT_EXPORT_METHOD(onStart:(RCTResponseSenderBlock)callback) {

	NSDictionary *state = [RNNNState sharedInstance].state;
	if (state) {
		NSLog(@"Reload");
		callback(@[state]);
	} else {
		NSLog(@"First load");
		callback(@[]);
	}
}

RCT_EXPORT_METHOD(setSiteMap:(NSDictionary *)map
			resolver:(RCTPromiseResolveBlock)resolve
			rejecter:(RCTPromiseRejectBlock)reject) {

	[RNNNState sharedInstance].state = map;

	NNStackNode *nodeObject = [NNNodeHelper nodeFromMap:map bridge:self.bridge];
	UIViewController *viewController = [nodeObject generate];

	dispatch_async(dispatch_get_main_queue(), ^{
		UIWindow *window = [RNNNState sharedInstance].window;
		window.rootViewController = viewController;
		[window makeKeyAndVisible];
	});

	resolve(@[]);
}

RCT_EXPORT_METHOD(push:(NSDictionary *)screen) {
	NNSingleNode *nodeObject = [NNNodeHelper nodeFromMap:screen bridge:self.bridge];
	UIViewController *viewController = [nodeObject generate];

	dispatch_async(dispatch_get_main_queue(), ^{
		UIViewController *findController = [UIApplication sharedApplication].keyWindow.rootViewController;
		while (![findController isKindOfClass:[UINavigationController class]] && findController.presentedViewController) {
			findController = findController.presentedViewController;
		}
		if ([findController isKindOfClass:[UINavigationController class]]) {
			UINavigationController *navigationController = (UINavigationController *)findController;
			[navigationController pushViewController:viewController animated:YES];
		}
	});

	NSMutableDictionary *newState = [RNNNState sharedInstance].state.mutableCopy;
	[newState[@"stack"] addObject:screen];
	[RNNNState sharedInstance].state = newState;
}

@end
