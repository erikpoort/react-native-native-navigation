//

// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNSplitNode.h"
#import "NNNodeHelper.h"
#import "NNSplitView.h"

static NSString *const kNode1Key = @"node1";
static NSString *const kNode2Key = @"node2";
static NSString *const kAxisKey = @"axis";

@interface NNSplitNode ()

@property (nonatomic, strong) RCTBridge *bridge;
@property (nonatomic, strong) id <NNNode> node1;
@property (nonatomic, strong) id <NNNode> node2;
@property (nonatomic, assign) UILayoutConstraintAxis axis;

@end

@implementation NNSplitNode

+ (NSString *)jsName
{
	return @"SplitView";
}

- (UIViewController <NNView> *)generate
{
	return [[NNSplitView alloc] initWithNode:self];
}

- (void)setData:(NSDictionary *)data
{
	[super setData:data];

	self.node1 = [NNNodeHelper.sharedInstance nodeFromMap:data[kNode1Key] bridge:self.bridge];
	self.node2 = [NNNodeHelper.sharedInstance nodeFromMap:data[kNode2Key] bridge:self.bridge];
	self.axis = [data[kAxisKey] isEqualToString:@"vertical"] ? 1 : 0;
}

- (NSDictionary *)data
{
	NSMutableDictionary *data = [super data].mutableCopy;
	data[kNode1Key] = self.node1.data;
	data[kNode2Key] = self.node2.data;
	data[kAxisKey] = self.axis == UILayoutConstraintAxisHorizontal ? @"horizontal" : @"vertical";
	return data.copy;
}

- (NSString *)title
{
	return self.node1.title;
}

@end