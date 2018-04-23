//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <React/RCTBridge.h>
#import "NNNodeHelper.h"
#import "NNStackNode.h"
#import "NNSingleNode.h"
#import "NNTabNode.h"
#import "NNSplitNode.h"
#import "NNDrawerNode.h"
#import "RNNNState.h"

static NSString *const kJSViewName = @"type";


@implementation NNNodeHelper
{
    NSArray<Class<NNNode>> *_internalNodes;
    NSArray<Class<NNNode>> *_externalNodes;
}

+ (instancetype)sharedInstance
{
    static dispatch_once_t once;
    static id sharedInstance;
    dispatch_once(&once, ^{
        sharedInstance = [[self alloc] init];
    });
    return sharedInstance;
}

- (instancetype)init
{
    self = [super init];
    if (self) {
        _internalNodes = @[
            [NNSingleNode class],
            [NNStackNode class],
            [NNTabNode class],
            [NNSplitNode class],
            [NNDrawerNode class],
        ];
        _externalNodes = @[];
    }
    return self;
}

- (void)addExternalNodes:(NSArray<Class<NNNode>> *)nodes
{
    _externalNodes = [_externalNodes arrayByAddingObjectsFromArray:nodes];
}

- (id<NNNode>)nodeFromMap:(NSDictionary *)map bridge:(RCTBridge *)bridge eventEmitter:(ReactNativeNativeEventEmitter *)eventEmitter
{
    if (map == nil || [map isEqual:[NSNull null]]) {
        return nil;
    }

    NSMutableArray<NSString *> *names = @[ [NNSingleNode jsName], [NNStackNode jsName], [NNTabNode jsName], [NNSplitNode jsName], [NNDrawerNode jsName] ].mutableCopy;
    NSMutableArray<Class<NNNode>> *classes = _internalNodes.mutableCopy;

    [_externalNodes enumerateObjectsUsingBlock:^(Class<NNNode> cls, NSUInteger idx, BOOL *stop) {
        if ([cls conformsToProtocol:@protocol(NNNode)]) {
            [names addObject:[cls jsName]];
            [classes addObject:cls.class];
        }
    }];

    NSString *name = map[kJSViewName];
    NSUInteger index = [names indexOfObject:name];
    if (index != NSNotFound) {
        Class nodeClass = classes[index];
        id<NNNode> nodeObject = (id<NNNode>)[[nodeClass alloc] init];
        [nodeObject setBridge:bridge];
        [nodeObject setData:map];
        [nodeObject setEventEmitter:eventEmitter];
        return nodeObject;
    }
    return nil;
}

- (NSDictionary<NSString *, id> *)constantsToExport
{
    NSMutableArray<Class<NNNode>> *classes = _internalNodes.mutableCopy;
    [classes addObjectsFromArray:_externalNodes];

    NSMutableDictionary *constants = @{}.mutableCopy;
    [classes enumerateObjectsUsingBlock:^(Class<NNNode> cls, NSUInteger idx, BOOL *stop) {
        [constants addEntriesFromDictionary:[cls constantsToExport]];
    }];

    return constants.copy;
}

@end
