//
//  AppDelegate.m
//  RNNativeNavigation
//
//  Created by Erik Poort on 06/08/2017.
//  Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "AppDelegate.h"
#import "NNSingleNode.h"
#import "NNStackNode.h"
#import "NNTabNode.h"

@interface AppDelegate ()

@end

@implementation AppDelegate

/**
 * type: Drawer
 * left: {}
 * center: {}
 * right: {}
 */

/**
 * type: Tab
 * tabs: [{}]
 */

/**
 * type: Stack
 * stack: [{}]
 */

/**
 * type: Split
 * orientation: vertical|horizontal
 * views: [{}]
 */

/**
 * type: Modal
 * view: {}
 */

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {

    id <NNNode> stack = [[NNStackNode alloc] initWithStack:@[
            [[NNTabNode alloc] initWithTabs:@[
                    [[NNSingleNode alloc] init],
                    [[NNSingleNode alloc] init],
                    [[NNSingleNode alloc] init],
            ] selectedTab:1],
            [[NNSingleNode alloc] init],
            [[NNSingleNode alloc] init],
            [[NNSingleNode alloc] init],
    ]];

    id <NNNode> tabs = [[NNTabNode alloc] initWithTabs:@[
            [[NNSingleNode alloc] init],
            [[NNSingleNode alloc] init],
            [[NNSingleNode alloc] init],
            stack,
    ] selectedTab:3];

    self.window = [[UIWindow alloc] init];
    self.window.rootViewController = [tabs generate];
    [self.window makeKeyAndVisible];

    return YES;
}

@end