//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <React/RCTRootView.h>
#import <React/RCTConvert.h>
#import "NNSingleView.h"
#import "NNSingleNode.h"
#import "NNNodeHelper.h"
#import "RNNNState.h"

NSString *const kShowModal = @"showModal";


@interface NNSingleView ()

@property (nonatomic, strong) NNSingleNode *singleNode;
@property (nonatomic, strong) RCTBridge *bridge;

@end


@implementation NNSingleView

- (instancetype)initWithBridge:(RCTBridge *)bridge node:(NNSingleNode *)node
{
    if (self = [super init]) {
        self.singleNode = node;
        self.bridge = bridge;
        self.title = self.singleNode.style[@"title"] ?: @"";
    }
    return self;
}

- (__kindof id<NNNode>)node
{
    return self.singleNode;
}

- (void)loadView
{
    self.view = [[RCTRootView alloc] initWithBridge:self.bridge moduleName:self.node.screenID initialProperties:nil];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];

    [self setColors:self.singleNode.style];
}

- (void)willMoveToParentViewController:(nullable UIViewController *)parent
{
    [super willMoveToParentViewController:parent];

    if (parent == nil) {
        NSArray *controllers = self.navigationController.viewControllers.copy;
        NSUInteger index = [controllers indexOfObject:self];
        UIViewController *controller;
        if (index != NSNotFound) {
            controller = controllers[index - 1];
        } else {
            controller = controllers.lastObject;
        }

        if ([controller isKindOfClass:[NNSingleView class]]) {
            NNSingleView *singleView = (NNSingleView *) controller;
            [self setColors:singleView.singleNode.style];
        }
    }
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];

    [self setColors:self.singleNode.style];

    if (self.singleNode.modal) {
        [self presentViewController:[self.singleNode.modal generate] animated:NO completion:nil];
    }
}

- (void)setColors:(NSDictionary *)style
{
    if ([style[@"barHidden"] boolValue]) {
        return;
    }

    NSString *barTintColorString = style[@"barTint"];
    if (barTintColorString) {
        UIColor *color = [RCTConvert UIColor:barTintColorString];
        self.navigationController.navigationBar.tintColor = color;
        self.navigationController.navigationBar.titleTextAttributes = @{
            NSForegroundColorAttributeName : color,
        };
    }

    BOOL barTransparent = [style[@"barTransparent"] boolValue];

    self.navigationController.navigationBar.translucent = barTransparent;

    if (barTransparent) {
        self.navigationController.navigationBar.shadowImage = [[UIImage alloc] init];
        [self.navigationController.navigationBar setBackgroundImage:[[UIImage alloc] init] forBarMetrics:UIBarMetricsDefault];
        self.edgesForExtendedLayout = UIRectEdgeAll;
    } else {
        self.navigationController.navigationBar.shadowImage = nil;
        [self.navigationController.navigationBar setBackgroundImage:nil forBarMetrics:UIBarMetricsDefault];

        NSString *barBackgroundColorString = style[@"barBackground"];
        if (barBackgroundColorString) {
            self.navigationController.navigationBar.barTintColor = [RCTConvert UIColor:barBackgroundColorString];
        }
        self.edgesForExtendedLayout = UIRectEdgeNone;
    }
}

- (UIViewController<NNView> *)viewForPath:(NSString *)path
{
    UIViewController<NNView> *modalController = (UIViewController<NNView> *) self.presentedViewController;
    if ([path isEqualToString:self.singleNode.screenID]) {
        return self;
    }
    if (modalController && [path rangeOfString:modalController.node.screenID].location == 0) {
        if (![modalController.node.screenID isEqualToString:path]) {
            return [modalController viewForPath:path];
        } else {
            return modalController;
        }
    }
    return nil;
}

- (void)callMethodWithName:(NSString *)methodName arguments:(NSDictionary *)arguments callback:(RCTResponseSenderBlock)callback
{
    NSMutableDictionary *methodDictionary = @{}.mutableCopy;
    methodDictionary[kShowModal] = [NSValue valueWithPointer:@selector(showModal:callback:)];

    SEL thisSelector = [methodDictionary[methodName] pointerValue];
    [self performSelector:thisSelector withObject:arguments withObject:callback];
}

- (void)showModal:(NSDictionary *)arguments callback:(RCTResponseSenderBlock)callback
{
    NNSingleNode *nodeObject = [NNNodeHelper.sharedInstance nodeFromMap:arguments[@"screen"] bridge:self.bridge];

    UIViewController<NNView> *rootController = (UIViewController<NNView> *) [UIApplication sharedApplication].keyWindow.rootViewController;

    NNSingleNode *singleNode = self.node;
    singleNode.modal = nodeObject;

    NSDictionary *newState = rootController.node.data;
    [RNNNState sharedInstance].state = newState;
    callback(@[ newState ]);

    __weak typeof(self) weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^{
        __strong typeof(weakSelf) self = weakSelf;
        UIViewController *viewController = [nodeObject generate];
        if (viewController) {
            [self presentViewController:viewController animated:YES completion:nil];
        }
    });
}

@end
