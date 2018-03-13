//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNStackView.h"
#import "NNStackNode.h"
#import "NNNodeHelper.h"
#import "RNNNState.h"

NSString *const kPush = @"push";
NSString *const kPop = @"pop";
NSString *const kPopTo = @"popTo";


@interface NNStackView () <UINavigationControllerDelegate>

@property (nonatomic, strong) NNStackNode *stackNode;

@end


@implementation NNStackView

- (instancetype)initWithNode:(NNStackNode *)node
{
    if (self = [super init]) {
        self.stackNode = node;
        self.navigationBar.translucent = NO;
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

- (NSString *)title
{
    return self.viewControllers.firstObject.title;
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
    NSDictionary *methodDictionary = @{
        kPush : [NSValue valueWithPointer:@selector(push:callback:)],
        kPop : [NSValue valueWithPointer:@selector(pop:callback:)],
        kPopTo : [NSValue valueWithPointer:@selector(popTo:callback:)],
    };

    SEL thisSelector = [methodDictionary[methodName] pointerValue];
    [self performSelector:thisSelector withObject:arguments withObject:callback];
}

- (void)push:(NSDictionary *)arguments callback:(RCTResponseSenderBlock)callback
{
    UIViewController<NNView> *rootController = (UIViewController<NNView> *)[UIApplication sharedApplication].keyWindow.rootViewController;
    id<NNNode> nodeObject = [NNNodeHelper.sharedInstance nodeFromMap:arguments[@"screen"] bridge:arguments[@"bridge"]];
    NSMutableArray *stack = self.stackNode.stack.mutableCopy;
    [stack addObject:nodeObject];
    self.stackNode.stack = stack;

    NSDictionary *newState = rootController.node.data;
    [RNNNState sharedInstance].state = newState;
    callback(@[ newState ]);

    __weak typeof(self) weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^{
        __strong typeof(weakSelf) self = weakSelf;
        UIViewController *viewController = [nodeObject generate];

        NSArray *viewControllers;
        if ([arguments[@"arguments"][@"reset"] boolValue]) {
            viewControllers = @[ viewController ];
        } else {
            viewControllers = [self.viewControllers arrayByAddingObject:viewController];
        }

        [self setViewControllers:viewControllers animated:YES];
    });
}

- (void)pop:(NSDictionary *)arguments callback:(RCTResponseSenderBlock)callback
{
    UIViewController<NNView> *rootController = (UIViewController<NNView> *)[UIApplication sharedApplication].keyWindow.rootViewController;
    NNStackNode *stackNode = self.node;
    NSMutableArray *stack = stackNode.stack.mutableCopy;
    [stack removeLastObject];
    stackNode.stack = stack;

    NSDictionary *newState = rootController.node.data;
    [RNNNState sharedInstance].state = newState;
    callback(@[ newState ]);

    __weak typeof(self) weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^{
        __strong typeof(weakSelf) self = weakSelf;
        [self popViewControllerAnimated:YES];
    });
}

- (void)popTo:(NSDictionary *)arguments callback:(RCTResponseSenderBlock)callback
{
    UIViewController<NNView> *rootController = (UIViewController<NNView> *)[UIApplication sharedApplication].keyWindow.rootViewController;

    UIViewController *foundController = [rootController viewForPath:arguments[@"path"]];
    if (!foundController) {
        return;
    }

    UINavigationController *navigationController = foundController.navigationController;
    if (!navigationController) {
        return;
    }

    NSUInteger index = [navigationController.viewControllers indexOfObject:foundController];
    if (index == NSNotFound) {
        return;
    }

    NSArray *viewControllers = [navigationController.viewControllers subarrayWithRange:NSMakeRange(0, index + 1)];

    __weak typeof(self) weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^{
        __strong typeof(weakSelf) self = weakSelf;
        [self setViewControllers:viewControllers animated:YES];
    });
}

#pragma mark - UINavigationControllerDelegate

- (void)navigationController:(UINavigationController *)navigationController didShowViewController:(UIViewController *)viewController animated:(BOOL)animated
{
    self.stackNode.stack = [self.stackNode.stack subarrayWithRange:NSMakeRange(0, self.viewControllers.count)];
}

@end
