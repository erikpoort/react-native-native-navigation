//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNStackView.h"
#import "NNNode.h"
#import "NNStackNode.h"

@interface NNStackView ()

@property (nonatomic, strong) NNStackNode *node;

@end

@implementation NNStackView

- (instancetype)initWithNode:(NNStackNode *)node {
    if (self = [super init]) {
		self.node = node;
        self.navigationBar.translucent = NO;
        NSMutableArray *viewControllers = [@[] mutableCopy];
        [node.stack enumerateObjectsUsingBlock:^(id <NNNode> view, NSUInteger idx, BOOL *stop) {
            [viewControllers addObject:[view generate]];
        }];
        self.viewControllers = [viewControllers copy];
    }

    return self;
}

- (NSString *)title {
    return self.viewControllers.lastObject.title;
}

- (NSString *)screenID
{
	return self.node.screenID;
}

- (UIViewController <NNView> *)viewForPath:(NSString *)path
{
	if ([path rangeOfString:self.screenID].location == 0) {
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

@end
