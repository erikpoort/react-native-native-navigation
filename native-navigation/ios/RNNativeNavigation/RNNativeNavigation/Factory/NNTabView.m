//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNTabView.h"
#import "NNNode.h"
#import "NNTabNode.h"
#import "RNNNState.h"

NSString *const kOpenTab = @"openTab";
NSString *const kRemoveTab = @"removeTab";


@interface NNTabView () <UITabBarDelegate>

@property (nonatomic, strong) NNTabNode *tabNode;
@property (nonatomic, strong) UIView *holder;
@property (nonatomic, strong) UITabBar *tabBar;
@property (nonatomic, strong) NSArray<UIViewController<NNView> *> *viewControllers;

@end


@implementation NNTabView

- (instancetype)initWithNode:(NNTabNode *)node
{
    if (self = [super init]) {
        self.tabNode = node;

        self.title = node.title;
        self.view.backgroundColor = [UIColor whiteColor];

        NSMutableArray *viewControllers = [@[] mutableCopy];
        NSMutableArray *items = [@[] mutableCopy];
        [node.tabs enumerateObjectsUsingBlock:^(id<NNNode> view, NSUInteger idx, BOOL *stop) {
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

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self showTabBarViewControllerForItem:self.tabBar.items[self.tabNode.selectedTab]];
}

- (void)showTabBarViewControllerForItem:(UITabBarItem *)item
{
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

- (__kindof id<NNNode>)node
{
    return self.tabNode;
}

- (UIViewController<NNView> *)viewForPath:(NSString *)path
{
    if ([path isEqualToString:self.tabNode.screenID]) {
        return self;
    }

    if ([path rangeOfString:self.node.screenID].location == 0) {
        UIViewController<NNView> *checkController;
        UIViewController<NNView> *foundController;

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
        } while (checkController != nil);

        if (![foundController.node.screenID isEqualToString:path]) {
            foundController = [foundController viewForPath:path];
        }

        return foundController;
    }

    return nil;
}

- (void)callMethodWithName:(NSString *)methodName arguments:(NSDictionary *)arguments callback:(RCTResponseSenderBlock)callback
{
    NSDictionary *methodDictionary = @{
        kOpenTab : [NSValue valueWithPointer:@selector(openTab:callback:)],
        kRemoveTab : [NSValue valueWithPointer:@selector(removeTab:callback:)],
    };

    SEL thisSelector = [methodDictionary[methodName] pointerValue];
    [self performSelector:thisSelector withObject:arguments withObject:callback];
}

- (void)openTab:(NSDictionary *)arguments callback:(RCTResponseSenderBlock)callback
{
    UIViewController<NNView> *rootController = (UIViewController<NNView> *)[UIApplication sharedApplication].keyWindow.rootViewController;
    NSUInteger index = [arguments[@"index"] unsignedIntegerValue];
    self.tabNode.selectedTab = index;

    NSDictionary *newState = rootController.node.data;
    [RNNNState sharedInstance].state = newState;

    __weak typeof(self) weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^{
        __strong typeof(weakSelf) self = weakSelf;
        UITabBarItem *item = self.tabBar.items[index];
        [self showTabBarViewControllerForItem:item];
    });
}

- (void)removeTab:(NSDictionary *)arguments callback:(RCTResponseSenderBlock)callback
{
    NSUInteger index = [arguments[@"index"] unsignedIntegerValue];
    BOOL animated = [arguments[@"animated"] boolValue];

    if (index >= self.tabBar.items.count) {
        return;
    }

    NSMutableArray *items = self.tabBar.items.mutableCopy;
    [items removeObjectAtIndex:index];

    NSMutableArray *tabs = self.tabNode.tabs.mutableCopy;
    [tabs removeObjectAtIndex:index];
    self.tabNode.tabs = tabs.copy;

    NSMutableArray *viewControllers = self.viewControllers.mutableCopy;
    [viewControllers removeObjectAtIndex:index];
    self.viewControllers = viewControllers.copy;

    UIViewController<NNView> *rootController = (UIViewController<NNView> *)[UIApplication sharedApplication].keyWindow.rootViewController;
    NSDictionary *newState = rootController.node.data;
    [RNNNState sharedInstance].state = newState;

    __weak typeof(self) weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^{
        __strong typeof(weakSelf) self = weakSelf;
        [self.tabBar setItems:items.copy animated:animated];
    });
}

#pragma mark - UITabBarDelegate

- (void)tabBar:(UITabBar *)tabBar didSelectItem:(UITabBarItem *)item
{
    if ([self.tabBar.items indexOfObject:item] == self.tabNode.selectedTab) {
        if ([self.viewControllers[self.tabNode.selectedTab] isKindOfClass:[UINavigationController class]]) {
            UINavigationController *navigationController = (UINavigationController *)self.viewControllers[self.tabNode.selectedTab];
            [navigationController popToRootViewControllerAnimated:YES];
        }
    } else {
        [self showTabBarViewControllerForItem:item];
    }
}

@end
