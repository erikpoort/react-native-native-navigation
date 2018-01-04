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
#import "NNBaseNode.h"
#import "ExternalNodesManager.h"

static NSString *const kJSViewName = @"type";

@implementation NNNodeHelper

+ (id <NNNode>)nodeFromMap:(NSDictionary *)map bridge:(RCTBridge *)bridge {
    if (map == nil || [map isEqual:[NSNull null]]) {
        return nil;
    }

    NSMutableArray <NSString *> *names = [@[[NNSingleNode jsName], [NNStackNode jsName], [NNTabNode jsName], [NNSplitNode jsName], [NNDrawerNode jsName]] mutableCopy];
    NSMutableArray <Class <NNNode>> *classes = [NSMutableArray arrayWithObjects:[NNSingleNode class],
                    [NNStackNode class],
                    [NNTabNode class],
                    [NNSplitNode class],
                    [NNDrawerNode class], nil];

    [ExternalNodesManager.sharedInstance.externalNodes enumerateObjectsUsingBlock:^(Class<NNNode> cls, NSUInteger idx, BOOL *stop) {
       if([cls conformsToProtocol:@protocol(NNNode)])
        {
            [names addObject:[cls jsName]];
            [classes addObject:cls.class];
        }
    }];

	NSString *name = map[kJSViewName];
	NSUInteger index = [names indexOfObject:name];
	if (index != NSNotFound) {
		Class nodeClass = classes[index];
		id <NNNode> nodeObject = (id <NNNode>)[[nodeClass alloc] init];
		[nodeObject setBridge:bridge];
		[nodeObject setData:map];
		return nodeObject;
	}
	return nil;
}

@end
