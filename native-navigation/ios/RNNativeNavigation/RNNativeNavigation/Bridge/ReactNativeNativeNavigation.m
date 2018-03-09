//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <React/RCTUtils.h>
#import "NNBaseNode.h"
#import "ReactNativeNativeNavigation.h"
#import "RNNNState.h"
#import "NNNodeHelper.h"
#import "NNStackNode.h"
#import "NNView.h"

#import <React/RCTDevMenu.h>
#import <React/RCTKeyCommands.h>

#if __has_include("RCTDevMenu.h")
#endif

static NSString *const kRNNN = @"RNNN";


@implementation ReactNativeNativeNavigation
RCT_EXPORT_MODULE();

@synthesize bridge = _bridge;

- (instancetype)init
{
    if (self = [super init]) {
        [RNNNState sharedInstance];
    }
    return self;
}

+ (void)addExternalNodes:(NSArray<Class<NNNode>> *)nodes
{
    NSMutableArray *nodesToLoad = [NSMutableArray new];

    for (Class<NNNode> cls in nodes) {
        if ([cls conformsToProtocol:@protocol(NNNode)]) {
            [nodesToLoad addObject:cls];
        }
    }
    [NNNodeHelper.sharedInstance addExternalNodes:nodesToLoad];
}

- (NSDictionary<NSString *, id> *)constantsToExport
{
    return [NNNodeHelper.sharedInstance constantsToExport];
}

// @formatter:off
RCT_EXPORT_METHOD(
    onStart
    : (RCTResponseSenderBlock)callback)
{
    // @formatter:on
    NSDictionary *state = [RNNNState sharedInstance].state;
    if (state == nil) {
        printf("%s %s\n", kRNNN.UTF8String, "First load");
        callback(@[]);
    } else {
        printf("%s %s\n", kRNNN.UTF8String, "Reload");
        callback(@[ state ]);
    }
}

// @formatter:off
RCT_EXPORT_METHOD(
    setSiteMap
    : (NSDictionary *)map
        resolver
    : (RCTPromiseResolveBlock)resolve
        rejecter
    : (RCTPromiseRejectBlock)reject)
{
    // @formatter:on
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

// @formatter:off
RCT_EXPORT_METHOD(
    callMethodOnNode
    : (NSString *)navigator
        methodName
    : (NSString *)methodName
        arguments
    : (NSDictionary *)arguments
        responseCallback
    : (RCTResponseSenderBlock)callback)
{
    // @formatter:on
    UIViewController<NNView> *rootController = (UIViewController<NNView> *)[UIApplication sharedApplication].keyWindow.rootViewController;
    __kindof UIViewController<NNView> *findController = [rootController viewForPath:navigator];
    if (!findController) return;

    NSMutableDictionary *newArguments = @{}.mutableCopy;
    newArguments[@"bridge"] = self.bridge;
    [newArguments addEntriesFromDictionary:arguments];

    if ([findController respondsToSelector:@selector(callMethodWithName:arguments:callback:)]) {
        [findController callMethodWithName:methodName arguments:newArguments callback:callback];
    }
}

// @formatter:off
RCT_EXPORT_METHOD(
    resetApplication)
{
    // @formatter:on
    [RNNNState sharedInstance].window.rootViewController = nil;
    [RNNNState sharedInstance].state = nil;
    [_bridge reload];
}

- (void)setBridge:(RCTBridge *)bridge
{
    _bridge = bridge;

    [self addDeveloperOptions];
}

- (void)addDeveloperOptions
{
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
                             }];
}

- (void)dealloc
{
    UIViewController<NNView> *viewController = (UIViewController<NNView> *)[RNNNState sharedInstance].window.rootViewController;
    [RNNNState sharedInstance].state = [viewController node].data;
}

@end
