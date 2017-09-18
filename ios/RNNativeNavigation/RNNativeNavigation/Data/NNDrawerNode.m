//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNDrawerNode.h"
#import "NNNodeHelper.h"
#import "NNDrawerView.h"

static NSString *const kLeftKey = @"left";
static NSString *const kCenterKey = @"center";
static NSString *const kRightKey = @"right";
static NSString *const kSideKey = @"side";

@interface NNDrawerNode ()

@property (nonatomic, strong) RCTBridge *bridge;
@property (nonatomic, strong) id <NNNode> leftNode;
@property (nonatomic, strong) id <NNNode> centerNode;
@property (nonatomic, strong) id <NNNode> rightNode;

@end

@implementation NNDrawerNode

+ (NSString *)jsName
{
	return @"DrawerView";
}

- (UIViewController <NNView> *)generate
{
	return [[NNDrawerView alloc] initWithNode:self];
}

- (void)setData:(NSDictionary *)data
{
	[super setData:data];

	self.leftNode = [NNNodeHelper nodeFromMap:data[kLeftKey] bridge:self.bridge];
	self.centerNode = [NNNodeHelper nodeFromMap:data[kCenterKey] bridge:self.bridge];
	self.rightNode = [NNNodeHelper nodeFromMap:data[kRightKey] bridge:self.bridge];

	NSArray *sides = @[@"center", @"left", @"right"];
	NSString *side = data[kSideKey];
	self.side = side ? [sides indexOfObject:side] : 0;
}

- (NSDictionary *)data
{
	NSMutableDictionary *data = [super data].mutableCopy;
	data[kLeftKey] = self.leftNode.data;
	data[kCenterKey] = self.centerNode.data;
	data[kRightKey] = self.rightNode.data;
	NSArray *sides = @[@"center", @"left", @"right"];
	data[kSideKey] = sides[self.side];
	return data.copy;
}

- (NSString *)title
{
	return self.centerNode.title;
}

@end
