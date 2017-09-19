//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNDrawerView.h"
#import "NNDrawerNode.h"

@interface NNDrawerView ()

@property (nonatomic, strong) NNDrawerNode *drawerNode;

@end

@implementation NNDrawerView

- (instancetype)initWithNode:(NNDrawerNode *)node
{
	NNDrawerNode *drawerNode = node;
	UIViewController <NNView> *leftController = [drawerNode.leftNode generate];
	UIViewController <NNView> *centerController = [drawerNode.centerNode generate];
	UIViewController <NNView> *rightController = [drawerNode.rightNode generate];

	if (self = [super initWithCenterViewController:centerController
					  leftDrawerViewController:leftController
					  rightDrawerViewController:rightController]) {
		self.drawerNode = drawerNode;
		self.title = node.title;
		self.openDrawerGestureModeMask = MMOpenDrawerGestureModeAll;
		self.closeDrawerGestureModeMask = MMCloseDrawerGestureModeAll;

		if (self.openSide != drawerNode.side) {
			[self openDrawerSide:drawerNode.side animated:NO completion:nil];
		}

		__weak NNDrawerView *weakSelf = self;
		[self setDrawerVisualStateBlock:^(MMDrawerController *drawerController, MMDrawerSide drawerSide, CGFloat percentVisible)
		{
			weakSelf.drawerNode.side = drawerSide;
		}];
	}
	return self;
}

- (__kindof id <NNNode>)node
{
	return self.drawerNode;
}

- (UIViewController <NNView> *)viewForPath:(NSString *)path
{
	UIViewController <NNView> *foundController;
	UIViewController <NNView> *leftController = (UIViewController <NNView> *)self.leftDrawerViewController;
	UIViewController <NNView> *centerController = (UIViewController <NNView> *)self.centerViewController;
	UIViewController <NNView> *rightController = (UIViewController <NNView> *)self.rightDrawerViewController;

	if (leftController && [path rangeOfString:leftController.node.screenID].location == 0) {
		foundController = leftController;
	} else if (centerController && [path rangeOfString:centerController.node.screenID].location == 0) {
		foundController = centerController;
	} else if (rightController && [path rangeOfString:rightController.node.screenID].location == 0) {
		foundController = rightController;
	}
	if (![foundController.node.screenID isEqualToString:path]) {
		foundController = [foundController viewForPath:path];
	}
	return foundController;

}

@end
