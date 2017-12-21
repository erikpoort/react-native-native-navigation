//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNSplitView.h"
#import "NNSplitNode.h"

@interface NNSplitView ()

@property (nonatomic, strong) NNSplitNode *splitNode;
@property (nonatomic, strong) UIViewController <NNView> *controller1;
@property (nonatomic, strong) UIViewController <NNView> *controller2;

@end

@implementation NNSplitView

- (instancetype)initWithNode:(NNSplitNode *)node {
	if (self = [super init]) {
		self.splitNode = node;

		self.title = node.title;

		self.controller1 = [self.splitNode.node1 generate];
		UIView *view1 = self.controller1.view;
		view1.translatesAutoresizingMaskIntoConstraints = NO;
		[self addChildViewController:self.controller1];
		[self.view addSubview:view1];
		[self.controller1 didMoveToParentViewController:self];

		self.controller2 = [self.splitNode.node2 generate];
		UIView *view2 = self.controller2.view;
		view2.translatesAutoresizingMaskIntoConstraints = NO;
		[self addChildViewController:self.controller2];
		[self.view addSubview:view2];
		[self.controller2 didMoveToParentViewController:self];

		NSMutableArray *axes = @[@"H", @"V"].mutableCopy;
		NSString *baseAxis = axes[self.splitNode.axis];
		[axes removeObjectAtIndex:self.splitNode.axis];
		NSString *oppositeAxis = axes.firstObject;

		NSDictionary *metrics;
		NSDictionary *views = NSDictionaryOfVariableBindings(view1, view2);
		[NSLayoutConstraint activateConstraints:[NSLayoutConstraint constraintsWithVisualFormat:[NSString stringWithFormat:@"%@:|-0-[view1]-0-|", oppositeAxis] options:0 metrics:metrics views:views]];
		[NSLayoutConstraint activateConstraints:[NSLayoutConstraint constraintsWithVisualFormat:[NSString stringWithFormat:@"%@:|-0-[view2]-0-|", oppositeAxis] options:0 metrics:metrics views:views]];
		[NSLayoutConstraint activateConstraints:[NSLayoutConstraint constraintsWithVisualFormat:[NSString stringWithFormat:@"%@:|-0-[view1]-0-[view2]-0-|", baseAxis] options:0 metrics:metrics views:views]];
		[NSLayoutConstraint activateConstraints:[NSLayoutConstraint constraintsWithVisualFormat:[NSString stringWithFormat:@"%@:[view1(==view2)]", baseAxis] options:0 metrics:metrics views:views]];
	}

	return self;
}

- (__kindof id <NNNode>)node {
	return self.splitNode;
}

- (UIViewController <NNView> *)viewForPath:(NSString *)path {
	UIViewController <NNView> *foundController;
	if ([path rangeOfString:self.controller1.node.screenID].location == 0) {
		foundController = self.controller1;
	} else if ([path rangeOfString:self.controller2.node.screenID].location == 0) {
		foundController = self.controller2;
	}
	if (![foundController.node.screenID isEqualToString:path]) {
		foundController = [foundController viewForPath:path];
	}
	return foundController;
}

@end
