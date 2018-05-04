//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNStackView.h"
#import "NNStackNode.h"
#import "NNNodeHelper.h"
#import "RNNNState.h"
#import "NNSingleView.h"
#import "NNSingleNode.h"

NSString *const kPush = @"push";
NSString *const kPop = @"pop";
NSString *const kPopTo = @"popTo";
NSString *const kPopToRoot = @"popToRoot";


@interface NNStackView () <UINavigationControllerDelegate>

@property (nonatomic, strong) NNStackNode *stackNode;

@end


@implementation NNStackView

- (instancetype)initWithNode:(NNStackNode *)node
{
    if (self = [super init]) {
        self.stackNode = node;
        NSMutableArray *viewControllers = [@[] mutableCopy];
        [node.stack enumerateObjectsUsingBlock:^(id<NNNode> view, NSUInteger idx, BOOL *stop) {
            UIViewController<NNView> *viewController = [view generate];
            [viewControllers addObject:viewController];
        }];
        self.viewControllers = [viewControllers copy];

        self.delegate = self;
    }

    return self;
}

- (__kindof id<NNNode>)node
{
    return self.stackNode;
}

- (UIViewController<NNView> *)viewForPath:(NSString *)path
{
    if ([path isEqualToString:self.stackNode.screenID]) {
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
    typedef void (^Block)(void);
    NSDictionary<NSString *, Block> *methodMap = @{
        kPush: ^{
            [self push:arguments callback:callback];
        },
        kPop: ^{
            [self pop];
        },
        kPopTo: ^{
            [self popTo:arguments];
        },
        kPopToRoot: ^{
            [self popToRootCallback];
        },
    };

    Block block = methodMap[methodName];
    if (block) {
        block();
    }
}

- (void)push:(NSDictionary *)arguments callback:(RCTResponseSenderBlock)callback
{
    UIViewController<NNView> *rootController = (UIViewController<NNView> *) [UIApplication sharedApplication].keyWindow.rootViewController;
    id<NNNode> nodeObject = [NNNodeHelper.sharedInstance nodeFromMap:arguments[@"screen"] bridge:self.stackNode.bridge];
    NSMutableArray *stack = self.stackNode.stack.mutableCopy;
    [stack addObject:nodeObject];
    self.stackNode.stack = stack;

    NSDictionary *newState = rootController.node.data;
    [RNNNState sharedInstance].state = newState;
    callback(@[newState]);

    NSDictionary *extraArguments = arguments[@"arguments"];

    __weak typeof(self) weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^{
        __strong typeof(weakSelf) self = weakSelf;
        UIViewController<NNView> *viewController = [nodeObject generate];

        if ([extraArguments[@"reset"] boolValue]) {
            NSArray *viewControllers = @[viewController];
            [self setViewControllers:viewControllers animated:YES];
        } else {
            if ([viewController.node isKindOfClass:[NNSingleNode class]]) {
                NNSingleNode *singleNode = viewController.node;
                NSString *backButtonTitle = singleNode.style[@"backButtonTitle"];
                if (backButtonTitle) {
                    if ([backButtonTitle isEqualToString:@""]) {
                        backButtonTitle = @" ";
                    }
                    self.viewControllers.lastObject.navigationItem.title = backButtonTitle;
                }
            }
            [self pushViewController:viewController animated:YES];
        }
    });
}

- (void)pop
{
    UIViewController<NNView> *rootController = (UIViewController<NNView> *) [UIApplication sharedApplication].keyWindow.rootViewController;
    NSMutableArray *stack = self.stackNode.stack.mutableCopy;
    [stack removeLastObject];
    self.stackNode.stack = stack;

    NSDictionary *newState = rootController.node.data;
    [RNNNState sharedInstance].state = newState;

    __weak typeof(self) weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^{
        __strong typeof(weakSelf) self = weakSelf;
        [self popViewControllerAnimated:YES];
    });
}

- (void)popTo:(NSDictionary *)arguments
{
    UIViewController<NNView> *rootController = (UIViewController<NNView> *) [UIApplication sharedApplication].keyWindow.rootViewController;

    UIViewController *foundController = [rootController viewForPath:arguments[@"path"]];
    if (!foundController) {
        return;
    }

    NSUInteger index = [self.viewControllers indexOfObject:foundController];
    if (index == NSNotFound) {
        return;
    }

    NSRange range = NSMakeRange(0, index + 1);
    self.stackNode.stack = [self.stackNode.stack subarrayWithRange:range];

    NSDictionary *newState = rootController.node.data;
    [RNNNState sharedInstance].state = newState;

    __weak typeof(self) weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^{
        __strong typeof(weakSelf) self = weakSelf;
        [self popToViewController:foundController animated:YES];
    });
}

- (void)popToRootCallback
{
    UIViewController<NNView> *rootController = (UIViewController<NNView> *) [UIApplication sharedApplication].keyWindow.rootViewController;

    self.stackNode.stack = @[self.stackNode.stack.firstObject];
    NSDictionary *newState = rootController.node.data;
    [RNNNState sharedInstance].state = newState;

    __weak typeof(self) weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^{
        __strong typeof(weakSelf) self = weakSelf;
        [self popToRootViewControllerAnimated:YES];
    });
}

#pragma mark - UINavigationControllerDelegate

- (void)navigationController:(UINavigationController *)navigationController didShowViewController:(UIViewController *)viewController animated:(BOOL)animated
{
    self.stackNode.stack = [self.stackNode.stack subarrayWithRange:NSMakeRange(0, self.viewControllers.count)];
}

- (void)navigationController:(UINavigationController *)navigationController willShowViewController:(UIViewController *)viewController animated:(BOOL)animated
{
    if ([viewController isKindOfClass:[NNSingleView class]]) {
        NNSingleView *singleView = (NNSingleView *) viewController;
        [navigationController setNavigationBarHidden:[singleView.singleNode.style[@"barHidden"] boolValue] animated:animated];
    }
}

@end
