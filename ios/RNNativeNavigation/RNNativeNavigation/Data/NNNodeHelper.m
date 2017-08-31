//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <React/RCTBridge.h>
#import "NNNodeHelper.h"
#import "NNStackNode.h"
#import "NNSingleNode.h"

static NSString *const kJSViewName = @"type";

@implementation NNNodeHelper

+ (id <NNNode>)nodeFromMap:(NSDictionary *)map bridge:(RCTBridge *)bridge {
	NSArray <NSString *> *names = @[[NNSingleNode jsName], [NNStackNode jsName]];
	NSArray <Class <NNNode>> *classes = @[[NNSingleNode class], [NNStackNode class]];
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
