//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNStackNode.h"
#import "NNStackView.h"
#import "NNNodeHelper.h"

static NSString *const kStackKey = @"stack";

@interface NNStackNode ()

@property (nonatomic, strong) RCTBridge *bridge;

@end

@implementation NNStackNode

+ (NSString *)jsName
{
    return @"StackView";
}

- (UIViewController <NNView> *)generate {
	return [[NNStackView alloc] initWithNode:self];
}

- (void)setData:(NSDictionary *)data
{
	[super setData:data];

    NSArray <NSDictionary *> *objects = data[kStackKey];
	NSMutableArray <NNNode> *tempStack = (NSMutableArray <NNNode> *)@[].mutableCopy;
	[objects enumerateObjectsUsingBlock:^(NSDictionary *obj, NSUInteger idx, BOOL *stop)
	{
		[tempStack addObject:[[NNNodeHelper sharedInstance] nodeFromMap:obj bridge:self.bridge]];
	}];
	self.stack = tempStack;
}

- (NSDictionary *)data
{
	NSMutableDictionary *data = [super data].mutableCopy;
	NSMutableArray *stack = @[].mutableCopy;
	[self.stack enumerateObjectsUsingBlock:^(id <NNNode> obj, NSUInteger idx, BOOL *stop)
	{
		[stack addObject:obj.data];
	}];
	data[kStackKey] = stack.copy;
	return data.copy;
}

- (NSString *)title
{
	return self.stack.lastObject.title;
}

+ (NSDictionary<NSString *, id> *)constantsToExport {
	return @{
			kPush: kPush
	};
}

@end
