//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNStackView.h"
#import "NNStackNode.h"

@interface NNStackView () <UINavigationControllerDelegate>

@property (nonatomic, strong) NNStackNode *stackNode;

@end

@implementation NNStackView

- (instancetype)initWithNode:(NNStackNode *)node {
    if (self = [super init]) {
		self.stackNode = node;
        self.navigationBar.translucent = NO;
        NSMutableArray *viewControllers = [@[] mutableCopy];
        [node.stack enumerateObjectsUsingBlock:^(id <NNNode> view, NSUInteger idx, BOOL *stop) {
			UIViewController <NNView> *viewController = [view generate];
            [viewControllers addObject:viewController];
        }];
        self.viewControllers = [viewControllers copy];

		self.delegate = self;
    }

    return self;
}

- (NSString *)title {
    return self.viewControllers.lastObject.title;
}

- (__kindof id <NNNode>)node
{
	return self.stackNode;
}

- (UIViewController <NNView> *)viewForPath:(NSString *)path
{
	if ([path rangeOfString:self.node.screenID].location == 0) {
		UIViewController <NNView> *checkController;
		UIViewController <NNView> *foundController;

		NSUInteger i = 0;
		do {
			if (i < self.viewControllers.count) {
				checkController = self.viewControllers[i++];

				if ([path rangeOfString:checkController.node.screenID].location == 0)
				{
					foundController = checkController;
				}
			} else {
				checkController = nil;
			}
		} while(checkController != nil);

		if (![foundController.node.screenID isEqualToString:path]) {
			foundController = [foundController viewForPath:path];
		}

		return foundController;
	}

	return nil;
}

#pragma mark - UINavigationControllerDelegate

- (void)navigationController:(UINavigationController *)navigationController didShowViewController:(UIViewController *)viewController animated:(BOOL)animated
{
	self.stackNode.stack = [self.stackNode.stack subarrayWithRange:NSMakeRange(0, self.viewControllers.count)];
}

@end
