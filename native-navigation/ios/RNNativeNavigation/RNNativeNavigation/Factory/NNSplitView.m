//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNSplitView.h"
#import "NNSplitNode.h"
#import "NNNodeHelper.h"
#import "RNNNState.h"

NSString *const kReplace = @"replace";


@interface NNSplitView ()

@property (nonatomic, strong) NNSplitNode *splitNode;
@property (nonatomic, strong) UIViewController<NNView> *controller1;
@property (nonatomic, strong) UIViewController<NNView> *controller2;
@property (nonatomic, strong) UIView *viewHolder1;
@property (nonatomic, strong) UIView *viewHolder2;

@end


@implementation NNSplitView

- (instancetype)initWithNode:(NNSplitNode *)node
{
    if (self = [super init]) {
        self.splitNode = node;

        self.title = node.title;


        NSMutableArray *axes = @[ @"H", @"V" ].mutableCopy;
        NSString *baseAxis = axes[self.splitNode.axis];
        [axes removeObjectAtIndex:self.splitNode.axis];
        NSString *oppositeAxis = axes.firstObject;

        self.viewHolder1 = [[UIView alloc] init];
        self.controller1 = [self.splitNode.node1 generate];
        UIView *view1 = self.viewHolder1;
        view1.translatesAutoresizingMaskIntoConstraints = NO;
        [self.view addSubview:view1];

        self.viewHolder2 = [[UIView alloc] init];
        self.controller2 = [self.splitNode.node2 generate];
        UIView *view2 = self.viewHolder2;
        view2.translatesAutoresizingMaskIntoConstraints = NO;
        [self.view addSubview:view2];

        NSDictionary *metrics;
        NSDictionary *views = NSDictionaryOfVariableBindings(view1, view2);
        [NSLayoutConstraint activateConstraints:[NSLayoutConstraint constraintsWithVisualFormat:[NSString stringWithFormat:@"%@:|-0-[view1]-0-|", oppositeAxis] options:0 metrics:metrics views:views]];
        [NSLayoutConstraint activateConstraints:[NSLayoutConstraint constraintsWithVisualFormat:[NSString stringWithFormat:@"%@:|-0-[view2]-0-|", oppositeAxis] options:0 metrics:metrics views:views]];
        [NSLayoutConstraint activateConstraints:[NSLayoutConstraint constraintsWithVisualFormat:[NSString stringWithFormat:@"%@:|-0-[view1]-0-[view2]-0-|", baseAxis] options:0 metrics:metrics views:views]];
        [NSLayoutConstraint activateConstraints:[NSLayoutConstraint constraintsWithVisualFormat:[NSString stringWithFormat:@"%@:[view1(==view2)]", baseAxis] options:0 metrics:metrics views:views]];
    }

    return self;
}

- (void)setController1:(UIViewController<NNView> *)controller1
{
    if (self.controller1) {
        [self.controller1 willMoveToParentViewController:nil];
        [self.controller1.view removeFromSuperview];
        [self.controller1 removeFromParentViewController];
    }

    _controller1 = controller1;

    UIView *view1 = self.controller1.view;
    view1.translatesAutoresizingMaskIntoConstraints = NO;
    [self addChildViewController:self.controller1];
    [self.viewHolder1 addSubview:view1];
    [self.controller1 didMoveToParentViewController:self];

    NSDictionary *views = NSDictionaryOfVariableBindings(view1);
    [NSLayoutConstraint activateConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:|[view1]|" options:0 metrics:nil views:views]];
    [NSLayoutConstraint activateConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"V:|[view1]|" options:0 metrics:nil views:views]];
}

- (void)setController2:(UIViewController<NNView> *)controller2
{
    if (self.controller2) {
        [self.controller2 willMoveToParentViewController:nil];
        [self.controller2.view removeFromSuperview];
        [self.controller2 removeFromParentViewController];
    }

    _controller2 = controller2;

    UIView *view2 = self.controller2.view;
    view2.translatesAutoresizingMaskIntoConstraints = NO;
    [self addChildViewController:self.controller2];
    [self.viewHolder2 addSubview:view2];
    [self.controller2 didMoveToParentViewController:self];

    NSDictionary *views = NSDictionaryOfVariableBindings(view2);
    [NSLayoutConstraint activateConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:|[view2]|" options:0 metrics:nil views:views]];
    [NSLayoutConstraint activateConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"V:|[view2]|" options:0 metrics:nil views:views]];
}

- (__kindof id<NNNode>)node
{
    return self.splitNode;
}

- (UIViewController<NNView> *)viewForPath:(NSString *)path
{
    UIViewController<NNView> *foundController;
    if ([path isEqualToString:self.splitNode.screenID]) {
        return self;
    }
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

- (void)callMethodWithName:(NSString *)methodName arguments:(NSDictionary *)arguments callback:(RCTResponseSenderBlock)callback
{
    NSDictionary *methodDictionary = @{
        kReplace : [NSValue valueWithPointer:@selector(replace:callback:)],
    };

    SEL thisSelector = [methodDictionary[methodName] pointerValue];
    [self performSelector:thisSelector withObject:arguments withObject:callback];
}

- (void)replace:(NSDictionary *)arguments callback:(RCTResponseSenderBlock)callback
{
    UIViewController<NNView> *rootController = (UIViewController<NNView> *)[UIApplication sharedApplication].keyWindow.rootViewController;
    id<NNNode> nodeObject = [NNNodeHelper.sharedInstance nodeFromMap:arguments[@"screen"] bridge:arguments[@"bridge"]];

    NSString *newPart = [nodeObject.screenID substringFromIndex:self.splitNode.screenID.length + 1];
    NSArray *splitArray = [newPart componentsSeparatedByString:@"/"];
    NSString *node = splitArray.firstObject;
    BOOL firstNode = [node isEqualToString:@"node1"];

    if (firstNode) {
        self.splitNode.node1 = nodeObject;
    } else {
        self.splitNode.node2 = nodeObject;
    }

    NSDictionary *newState = rootController.node.data;
    [RNNNState sharedInstance].state = newState;
    callback(@[ newState ]);

    __weak typeof(self) weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^{
        __strong typeof(weakSelf) self = weakSelf;
        UIViewController<NNView> *viewController = [nodeObject generate];
        if (firstNode) {
            self.controller1 = viewController;
        } else {
            self.controller2 = viewController;
        }
    });
}

@end
