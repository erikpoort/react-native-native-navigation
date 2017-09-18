//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNTabView.h"
#import "NNNode.h"
#import "NNTabNode.h"

@interface NNTabView () <UITabBarDelegate>

@property (nonatomic, strong) NNTabNode *tabNode;
@property (nonatomic, strong) UIView *holder;
@property (nonatomic, strong) UITabBar *tabBar;
@property (nonatomic, strong) NSArray <UIViewController <NNView> *> *viewControllers;

@end

@implementation NNTabView

- (instancetype)initWithNode:(NNTabNode *)node {
    if (self = [super init]) {
        self.tabNode = node;

		self.title = node.title;
		self.view.backgroundColor = [UIColor whiteColor];

		NSMutableArray *viewControllers = [@[] mutableCopy];
        NSMutableArray *items = [@[] mutableCopy];
        [node.tabs enumerateObjectsUsingBlock:^(id <NNNode> view, NSUInteger idx, BOOL *stop) {
            UIViewController *viewController = [view generate];
            [viewControllers addObject:viewController];
            UITabBarItem *tabBarItem = [[UITabBarItem alloc] initWithTitle:viewController.title image:nil tag:idx];
            [items addObject:tabBarItem];
        }];
        self.viewControllers = [viewControllers copy];

        self.holder = [[UIView alloc] init];
        self.holder.translatesAutoresizingMaskIntoConstraints = NO;
        [self.view addSubview:self.holder];

        self.tabBar = [[UITabBar alloc] init];
        self.tabBar.translatesAutoresizingMaskIntoConstraints = NO;
        self.tabBar.items = [items copy];
        self.tabBar.delegate = self;
        [self.view addSubview:self.tabBar];

        NSDictionary *views = NSDictionaryOfVariableBindings(_holder, _tabBar);
        [NSLayoutConstraint activateConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:|-0-[_holder]-0-|" options:0 metrics:nil views:views]];
        [NSLayoutConstraint activateConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:|-0-[_tabBar]-0-|" options:0 metrics:nil views:views]];
        [NSLayoutConstraint activateConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"V:|-0-[_holder]-0-[_tabBar]-0-|" options:0 metrics:nil views:views]];
    }

    return self;
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self showTabBarViewControllerForItem:self.tabBar.items[self.tabNode.selectedTab]];
}

- (void)showTabBarViewControllerForItem:(UITabBarItem *)item {
    [self.tabBar setSelectedItem:item];

    self.tabNode.selectedTab = [self.tabBar.items indexOfObject:item];

    while (self.holder.subviews.count) {
        [self.holder.subviews.firstObject removeFromSuperview];
    }

    UIViewController *viewController = self.viewControllers[self.tabNode.selectedTab];
    UIView *view = viewController.view;
    view.translatesAutoresizingMaskIntoConstraints = NO;
    [self addChildViewController:viewController];
    [self.holder addSubview:viewController.view];
    [viewController didMoveToParentViewController:self];

    NSDictionary *views = NSDictionaryOfVariableBindings(view);
    [NSLayoutConstraint activateConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:|-0-[view]-0-|" options:0 metrics:nil views:views]];
    [NSLayoutConstraint activateConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"V:|-0-[view]-0-|" options:0 metrics:nil views:views]];
}

- (__kindof id <NNNode>)node {
    return self.tabNode;
}

- (UIViewController <NNView> *)viewForPath:(NSString *)path {
    if ([path rangeOfString:self.node.screenID].location == 0) {
		UIViewController <NNView> *checkController;
		UIViewController <NNView> *foundController;

		NSUInteger i = 0;
		do {
			if (i < self.viewControllers.count) {
				checkController = self.viewControllers[i++];

				if ([path rangeOfString:checkController.node.screenID].location == 0) {
					foundController = checkController;
					checkController = nil;
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

#pragma mark - UITabBarDelegate

- (void)tabBar:(UITabBar *)tabBar didSelectItem:(UITabBarItem *)item {
    if ([self.tabBar.items indexOfObject:item] == self.tabNode.selectedTab) {
        if ([self.viewControllers[self.tabNode.selectedTab] isKindOfClass:[UINavigationController class]]) {
            UINavigationController *navigationController = (UINavigationController *) self.viewControllers[self.tabNode.selectedTab];
            [navigationController popToRootViewControllerAnimated:YES];
        }
    } else {
        [self showTabBarViewControllerForItem:item];
    }
}

@end
