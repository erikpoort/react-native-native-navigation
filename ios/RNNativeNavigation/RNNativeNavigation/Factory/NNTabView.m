//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNTabView.h"
#import "NNNode.h"

@interface NNTabView () <UITabBarDelegate>
@end

@implementation NNTabView {
    UIView *_holder;
    UITabBar *_tabBar;
    NSUInteger _selectedTab;
    NSArray <UIViewController *> *_viewControllers;
}

- (instancetype)initWithTabs:(NSArray <id <NNNode>> *)tabs selectedTab:(NSUInteger)tab {
    self = [super init];

    if (self) {
        NSMutableArray *viewControllers = [@[] mutableCopy];
        NSMutableArray *items = [@[] mutableCopy];
        [tabs enumerateObjectsUsingBlock:^(id <NNNode> view, NSUInteger idx, BOOL *stop) {
            UIViewController *viewController = [view generate];
            [viewControllers addObject:viewController];
            UITabBarItem *tabBarItem = [[UITabBarItem alloc] initWithTitle:viewController.title image:nil tag:idx];
            [items addObject:tabBarItem];
        }];
        _viewControllers = [viewControllers copy];
        _selectedTab = tab;

        _holder = [[UIView alloc] init];
        _holder.translatesAutoresizingMaskIntoConstraints = NO;
        [self.view addSubview:_holder];

        _tabBar = [[UITabBar alloc] init];
        _tabBar.translatesAutoresizingMaskIntoConstraints = NO;
        _tabBar.items = [items copy];
        _tabBar.delegate = self;
        [self.view addSubview:_tabBar];

        NSDictionary *views = NSDictionaryOfVariableBindings(_holder, _tabBar);
        [NSLayoutConstraint activateConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:|-0-[_holder]-0-|" options:0 metrics:nil views:views]];
        [NSLayoutConstraint activateConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:|-0-[_tabBar]-0-|" options:0 metrics:nil views:views]];
        [NSLayoutConstraint activateConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"V:|-0-[_holder]-0-[_tabBar]-0-|" options:0 metrics:nil views:views]];
    }

    return self;
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self showTabBarViewControllerForItem:_tabBar.items[_selectedTab]];
}

- (void)showTabBarViewControllerForItem:(UITabBarItem *)item {
    [_tabBar setSelectedItem:item];

    _selectedTab = [_tabBar.items indexOfObject:item];

    while (_holder.subviews.count) {
        [_holder.subviews.firstObject removeFromSuperview];
    }

    UIViewController *viewController = _viewControllers[_selectedTab];
    UIView *view = viewController.view;
    view.translatesAutoresizingMaskIntoConstraints = NO;
    [self addChildViewController:viewController];
    [_holder addSubview:viewController.view];
    [viewController didMoveToParentViewController:self];

    NSDictionary *views = NSDictionaryOfVariableBindings(view);
    [NSLayoutConstraint activateConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:|-0-[view]-0-|" options:0 metrics:nil views:views]];
    [NSLayoutConstraint activateConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"V:|-0-[view]-0-|" options:0 metrics:nil views:views]];
}

#pragma mark - UITabBarDelegate

- (void)tabBar:(UITabBar *)tabBar didSelectItem:(UITabBarItem *)item {
    if ([_tabBar.items indexOfObject:item] == _selectedTab) {
        if ([_viewControllers[_selectedTab] isKindOfClass:[UINavigationController class]]) {
            UINavigationController *navigationController = (UINavigationController *) _viewControllers[_selectedTab];
            [navigationController popToRootViewControllerAnimated:YES];
        }
    } else {
        [self showTabBarViewControllerForItem:item];
    }
}

@end
