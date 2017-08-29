//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNStackNode.h"
#import "NNStackView.h"
#import "NNNodeHelper.h"

static NSString *const kStackKey = @"stack";

@interface NNStackNode ()

@property (nonatomic, strong) RCTBridge *bridge;
@property (nonatomic, copy) NSArray <id <NNNode>> *stack;

@end

@implementation NNStackNode

+ (NSString *)jsName
{
    return @"StackView";
}

- (void)setData:(NSDictionary *)data
{
    NSArray <NSDictionary *> *objects = data[kStackKey];
	NSMutableArray <NNNode> *tempStack = (NSMutableArray <NNNode> *)@[].mutableCopy;
	[objects enumerateObjectsUsingBlock:^(NSDictionary *obj, NSUInteger idx, BOOL *stop)
	{
		[tempStack addObject:[NNNodeHelper nodeFromMap:obj bridge:self.bridge]];
	}];
	self.stack = tempStack;
}

- (UIViewController *)generate {
    return [[NNStackView alloc] initWithStack:self.stack];
}

@end
