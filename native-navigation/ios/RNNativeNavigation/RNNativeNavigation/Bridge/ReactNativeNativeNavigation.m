//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <React/RCTUtils.h>
#import "NNBaseNode.h"
#import "ReactNativeNativeNavigation.h"
#import "RNNNState.h"
#import "NNSingleNode.h"
#import "NNNodeHelper.h"
#import "NNStackNode.h"
#import "NNView.h"
#import "NNSingleView.h"
#import "NNDrawerNode.h"
#import "NNDrawerView.h"
#import "UIViewController+MMDrawerController.h"

#import <React/RCTDevMenu.h>
#import <React/RCTKeyCommands.h>

#if __has_include("RCTDevMenu.h")
#endif

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

+ (void)addExternalNodes:(NSArray <Class <NNNode>> *)nodes {
    NSMutableArray *nodesToLoad = [NSMutableArray new];

    for (Class <NNNode> cls in nodes) {
        if ([cls conformsToProtocol:@protocol(NNNode)]) {
            [nodesToLoad addObject:cls];
        }
    }
    [NNNodeHelper.sharedInstance addExternalNodes:nodesToLoad];
}

RCT_EXPORT_METHOD(onStart:
    (RCTResponseSenderBlock) callback) {
    NSDictionary *state = [RNNNState sharedInstance].state;
    if (state == nil) {
        printf("%s %s\n", kRNNN.UTF8String, "First load");
        callback(@[]);
    } else {
        printf("%s %s\n", kRNNN.UTF8String, "Reload");
        callback(@[state]);
    }
}

RCT_EXPORT_METHOD(setSiteMap:
    (NSDictionary *) map
            resolver:
            (RCTPromiseResolveBlock) resolve
            rejecter:
            (RCTPromiseRejectBlock) reject) {
    dispatch_async(dispatch_get_main_queue(), ^{
        [RNNNState sharedInstance].state = map;

        NNStackNode *nodeObject = [NNNodeHelper.sharedInstance nodeFromMap:map bridge:self.bridge];
        UIViewController *viewController = [nodeObject generate];

        UIWindow *window = [RNNNState sharedInstance].window;
        window.rootViewController = viewController;
        [window makeKeyAndVisible];
    });

    resolve(@[]);
}

RCT_EXPORT_METHOD(callMethodOnNode:
    (NSString *) path
            navigator:
            (NSString *) navigator
            methodName:
            (NSString *) methodName
            arguments:
            (NSDictionary *) arguments
            responseCallback:
            (RCTResponseSenderBlock) callback) {

    NNSingleNode *nodeObject = [NNNodeHelper.sharedInstance nodeFromMap:arguments[@"screen"] bridge:self.bridge];

    UIViewController <NNView> *rootController = (UIViewController <NNView> *) [UIApplication sharedApplication].keyWindow.rootViewController;
    NSString *parentPath = nodeObject.screenID.stringByDeletingLastPathComponent;
    __kindof UIViewController <NNView> *findController = [rootController viewForPath:parentPath];
    if (!findController) return;

    NSMutableDictionary *newArguemnts = @{}.mutableCopy;
    newArguemnts[@"bridge"] = self.bridge;
    [newArguemnts addEntriesFromDictionary:arguments];

    [findController callMethodWithName:methodName arguments:newArguemnts callback:^(NSArray *response) {
        callback(response);
    }];


}

RCT_EXPORT_METHOD(showModal:
    (NSDictionary *) screen
            registerCallback:
            (RCTResponseSenderBlock) callback) {
    NNSingleNode *nodeObject = [NNNodeHelper.sharedInstance nodeFromMap:screen bridge:self.bridge];

    UIViewController <NNView> *rootController = (UIViewController <NNView> *) [UIApplication sharedApplication].keyWindow.rootViewController;
    NSString *parentPath = nodeObject.screenID.stringByDeletingLastPathComponent.stringByDeletingLastPathComponent;
    NNSingleView *findController = (NNSingleView *) [rootController viewForPath:parentPath];
    if (!findController) return;

    NNSingleNode *singleNode = findController.node;
    singleNode.modal = nodeObject;

    NSDictionary *newState = rootController.node.data;
    [RNNNState sharedInstance].state = newState;
    callback(@[newState]);

    dispatch_async(dispatch_get_main_queue(), ^{
        UIViewController *viewController = [nodeObject generate];
        if (viewController) {
            [findController presentViewController:viewController animated:YES completion:nil];
        }
    });
}

RCT_EXPORT_METHOD(resetApplication) {
    [RNNNState sharedInstance].window.rootViewController = nil;
    [RNNNState sharedInstance].state = nil;
    [_bridge reload];
}

- (void)setBridge:(RCTBridge *)bridge {
    _bridge = bridge;

    [self addDeveloperOptions];
}

- (void)addDeveloperOptions {
#if __has_include("RCTDevMenu.h")
#endif
    __weak ReactNativeNativeNavigation *weakSelf = self;
    [_bridge.devMenu addItem:[RCTDevMenuItem buttonItemWithTitle:@"Reset navigation" handler:^{
        [weakSelf resetApplication];
    }]];

    [[RCTKeyCommands sharedInstance]
            registerKeyCommandWithInput:@"e"
                          modifierFlags:UIKeyModifierCommand
                                 action:^(UIKeyCommand *command) {
                                     [weakSelf resetApplication];
                                 }
    ];
}

- (void)dealloc {
    UIViewController <NNView> *viewController = (UIViewController <NNView> *) [RNNNState sharedInstance].window.rootViewController;
    [RNNNState sharedInstance].state = [viewController node].data;
}

@end
