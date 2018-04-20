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
NSString *const kUpdateStyle = @"updateStyle";


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
    [self adaptStyle];
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
            NSForegroundColorAttributeName: color,
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
    typedef void (^Block)(void);
    NSDictionary<NSString *, Block> *methodMap = @{
        kShowModal: ^{
            [self showModal:arguments callback:callback];
        },
        kUpdateStyle: ^{
            [self updateStyle:arguments];
        }
    };

    Block block = methodMap[methodName];
    if (block) {
        block();
    }
}

- (void)showModal:(NSDictionary *)arguments callback:(RCTResponseSenderBlock)callback
{
    NNSingleNode *nodeObject = [NNNodeHelper.sharedInstance nodeFromMap:arguments[@"screen"] bridge:self.bridge];

    UIViewController<NNView> *rootController = (UIViewController<NNView> *) [UIApplication sharedApplication].keyWindow.rootViewController;

    self.singleNode.modal = nodeObject;

    NSDictionary *newState = rootController.node.data;
    [RNNNState sharedInstance].state = newState;
    callback(@[newState]);

    __weak typeof(self) weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^{
        __strong typeof(weakSelf) self = weakSelf;
        UIViewController *viewController = [nodeObject generate];
        if (viewController) {
            [self presentViewController:viewController animated:YES completion:nil];
        }
    });
}

- (void)updateStyle:(NSDictionary *)arguments {
    NSMutableDictionary *style = self.singleNode.style.mutableCopy;
    NSString *title = arguments[@"title"];
    if (title) {
        style[@"title"] = title;
    }
    NSString *barTintString = arguments[@"barTint"];
    if (barTintString) {
        style[@"barTint"] = [RCTConvert UIColor:barTintString];
    }
    NSString *barBackgroundString = arguments[@"barBackground"];
    if (barBackgroundString) {
        style[@"barBackground"] = [RCTConvert UIColor:barBackgroundString];
    }
    self.singleNode.style = style.copy;

    [self adaptStyle];
}

- (void)adaptStyle {
    __weak typeof(self) weakSelf = self;
    dispatch_async(dispatch_get_main_queue(), ^{
        __strong typeof(weakSelf) self = weakSelf;
        NSString *title = self.singleNode.style[@"title"];
        if (title) {
            self.title = title;
        }

        NSMutableDictionary *textAttributes = @{}.mutableCopy;

        UIColor *barTintColor = self.singleNode.style[@"barTint"];
        if (barTintColor) {
            textAttributes[NSForegroundColorAttributeName] = barTintColor;
            self.navigationController.navigationBar.tintColor = barTintColor;
        }
        UIColor *barBackgroundColor = self.singleNode.style[@"barBackground"];
        NSString *barFontName = self.singleNode.style[@"barFont"];
        NSNumber *barFontSize = self.singleNode.style[@"barFontSize"];
        if (barFontName && barFontSize) {
            UIFont *barFont = [UIFont fontWithName:barFontName size:barFontSize.floatValue];
            textAttributes[NSFontAttributeName] = barFont;
        }

        if (textAttributes.count > 0) {
            self.navigationController.navigationBar.titleTextAttributes = textAttributes.copy;
        }

        if (barBackgroundColor) {
            self.navigationController.navigationBar.barTintColor = barBackgroundColor;
        }
    });
}

@end
