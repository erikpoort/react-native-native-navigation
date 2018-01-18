//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <React/RCTRootView.h>
#import "NNSingleView.h"
#import "NNSingleNode.h"
#import "NNStackNode.h"
#import "NNNodeHelper.h"
#import "RNNNState.h"

@interface NNSingleView ()

@property (nonatomic, strong) NNSingleNode *singleNode;
@property (nonatomic, strong) RCTBridge *bridge;

@end

@implementation NNSingleView

- (instancetype)initWithBridge:(RCTBridge *)bridge node:(NNSingleNode *)node {
    if (self = [super init]) {
        self.singleNode = node;
        self.bridge = bridge;
        self.title = node.title;
        if (node.lazyLoad) {
            [self loadViewIfNeeded];
        }
    }
    return self;
}

- (__kindof id <NNNode>)node
{
    return self.singleNode;
}

- (void)loadView {
    self.view = [[RCTRootView alloc] initWithBridge:self.bridge moduleName:self.node.screenID initialProperties:nil];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewWillAppear:animated];

    if (self.singleNode.modal) {
        [self presentViewController:[self.singleNode.modal generate] animated:NO completion:nil];
    }
}

- (UIViewController <NNView> *)viewForPath:(NSString *)path
{
	UIViewController <NNView> *modalController = (UIViewController <NNView> *)self.presentedViewController;
	if (modalController && [path rangeOfString:modalController.node.screenID].location == 0) {
		if (![modalController.node.screenID isEqualToString:path]) {
			return (UIViewController <NNView> *)[modalController viewForPath:path];
		} else {
			return modalController;
		}
	}
	return self;
}

- (void)callMethodWithName:(NSString *)methodName arguments:(NSDictionary *)arguments callback:(void (^)(NSArray *))callback {
    NSMutableDictionary *methodDictionary = @{}.mutableCopy;
    methodDictionary[@"push"] = [NSValue valueWithPointer:@selector(push:callback:)];

    SEL thisSelector = [methodDictionary[methodName] pointerValue];
    [self performSelector:thisSelector withObject:arguments withObject:^(NSArray *array){
        callback(array);
    }];
}

- (void)push: (NSDictionary *) arguments callback: (void(^)(NSArray *)) callback{
    UIViewController <NNView> *rootController = (UIViewController <NNView> *) [UIApplication sharedApplication].keyWindow.rootViewController;
    NNSingleNode *nodeObject = [NNNodeHelper.sharedInstance nodeFromMap:arguments[@"screen"] bridge:self.bridge];
    UINavigationController <NNView> *navigationController = (UINavigationController <NNView> *) self.navigationController;
    NNStackNode *stackNode = navigationController.node;
    NSMutableArray *stack = stackNode.stack.mutableCopy;
    [stack addObject:nodeObject];
    stackNode.stack = stack;

    NSDictionary *newState = rootController.node.data;
    [RNNNState sharedInstance].state = newState;
    callback(@[newState]);

    dispatch_async(dispatch_get_main_queue(), ^{
        UIViewController *viewController = [nodeObject generate];
        if (navigationController && viewController) {
            [navigationController pushViewController:viewController animated:YES];
        }
    });
}

@end
