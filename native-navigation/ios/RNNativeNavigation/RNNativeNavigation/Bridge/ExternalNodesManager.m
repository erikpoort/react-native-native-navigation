//
// Created by Juan Curti on 1/4/18.
// Copyright (c) 2018 MediaMonks. All rights reserved.
//

#import "ExternalNodesManager.h"
#import "NNBaseNode.h"
#import "NNNode.h"
#import <React/RCTBridge.h>

@implementation ExternalNodesManager
@synthesize externalNodes;

+ (instancetype)sharedInstance
{
    static dispatch_once_t once;
    static id sharedInstance;
    dispatch_once(&once, ^
    {
        sharedInstance = [[self alloc] init];
    });
    return sharedInstance;
}

- (void)setExternalNodes:(NSArray <Class <NNNode>> *)nodes {
    externalNodes = [[NSMutableArray alloc] init];
    [externalNodes addObjectsFromArray:nodes];

}

@end