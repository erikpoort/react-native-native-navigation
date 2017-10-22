//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <React/RCTUtils.h>
#import "ReactNativeNativeNavigation.h"
#import "RNNNState.h"
#import "NNSingleNode.h"
#import "NNNodeHelper.h"
#import "NNStackNode.h"
#import "NNView.h"
#import "NNSingleView.h"

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
	if (state == nil) {
		NSLog(@"%@ %@", kRNNN, @"First load");
		callback(@[]);
	} else {
		NSLog(@"%@ %@", kRNNN, @"Reload");
		callback(@[state]);
	}
}

RCT_EXPORT_METHOD(setSiteMap:(NSDictionary *)map
			resolver:(RCTPromiseResolveBlock)resolve
			rejecter:(RCTPromiseRejectBlock)reject) {
	dispatch_async(dispatch_get_main_queue(), ^{
		[RNNNState sharedInstance].state = map;

		NNStackNode *nodeObject = [NNNodeHelper nodeFromMap:map bridge:self.bridge];
		UIViewController *viewController = [nodeObject generate];

		UIWindow *window = [RNNNState sharedInstance].window;
		window.rootViewController = viewController;
		[window makeKeyAndVisible];
	});

	resolve(@[]);
}

RCT_EXPORT_METHOD(push:(NSDictionary *)screen registerCallback:(RCTResponseSenderBlock)callback) {
	NNSingleNode *nodeObject = [NNNodeHelper nodeFromMap:screen bridge:self.bridge];
	UIViewController *viewController = [nodeObject generate];

	UIViewController <NNView> *rootController = (UIViewController <NNView> *)[UIApplication sharedApplication].keyWindow.rootViewController;
	UIViewController <NNView> *findController = [rootController viewForPath:nodeObject.screenID.stringByDeletingLastPathComponent];
	if (!findController) return;

	UINavigationController <NNView> *navigationController = (UINavigationController <NNView> *)findController.navigationController;
	NNStackNode *stackNode = navigationController.node;
	NSMutableArray *stack = stackNode.stack.mutableCopy;
	[stack addObject:nodeObject];
	stackNode.stack = stack;

	NSDictionary *newState = rootController.node.data;
	[RNNNState sharedInstance].state = newState;
	callback(@[newState]);

	dispatch_async(dispatch_get_main_queue(), ^{
		if (navigationController && viewController) {
			[navigationController pushViewController:viewController animated:YES];
		}
	});
}

RCT_EXPORT_METHOD(showModal:(NSDictionary *)screen registerCallback:(RCTResponseSenderBlock)callback) {
	NNSingleNode *nodeObject = [NNNodeHelper nodeFromMap:screen bridge:self.bridge];
	UIViewController *viewController = [nodeObject generate];

    dispatch_async(dispatch_get_main_queue(), ^{
        UIViewController <NNView> *rootController = (UIViewController <NNView> *)[UIApplication sharedApplication].keyWindow.rootViewController;
        NSString *parentPath = nodeObject.screenID.stringByDeletingLastPathComponent.stringByDeletingLastPathComponent;
        NNSingleView *findController = (NNSingleView *)[rootController viewForPath:parentPath];
        if (!findController) return;

        NNSingleNode *singleNode = findController.node;
        singleNode.modal = nodeObject;

        NSDictionary *newState = rootController.node.data;
        [RNNNState sharedInstance].state = newState;
        callback(@[newState]);

		if (viewController) {
			[findController presentViewController:viewController animated:YES completion:nil];
		}
	});
}

- (void)dealloc
{
	UIViewController <NNView> *viewController = (UIViewController <NNView> *)[UIApplication sharedApplication].keyWindow.rootViewController;
	[RNNNState sharedInstance].state = [viewController node].data;
}

@end
