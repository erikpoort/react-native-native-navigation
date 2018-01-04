//
// Created by Juan Curti on 1/4/18.
// Copyright (c) 2018 MediaMonks. All rights reserved.
//

#import <Foundation/Foundation.h>

@class NNBaseNode;
@protocol NNNode;


@interface ExternalNodesManager : NSObject
@property (nonatomic, retain) NSMutableArray <Class <NNNode>> *externalNodes;

+ (instancetype)sharedInstance;
- (void)setExternalNodes:(NSArray <NNBaseNode*> *)nodes;
@end