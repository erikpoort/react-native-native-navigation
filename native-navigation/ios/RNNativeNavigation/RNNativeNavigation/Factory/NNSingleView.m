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
        if([path isEqualToString:self.singleNode.screenID]) return self;
		if (![modalController.node.screenID isEqualToString:path]) {
			return [modalController viewForPath:path];
		} else {
			return modalController;
		}
	}
	return self;
}

- (void)callMethodWithName:(NSString *)methodName arguments:(NSDictionary *)arguments callback:(void (^)(NSArray *))callback {
    NSMutableDictionary *methodDictionary = @{}.mutableCopy;
    methodDictionary[@"showModal"] = [NSValue valueWithPointer:@selector(showModal:callback:)];

    SEL thisSelector = [methodDictionary[methodName] pointerValue];
    [self performSelector:thisSelector withObject:arguments withObject:^(NSArray *array){
        callback(array);
    }];
}

- (void)showModal: (NSDictionary *) arguments callback: (void(^)(NSArray *)) callback{
    NNSingleNode *nodeObject = [NNNodeHelper.sharedInstance nodeFromMap:arguments[@"screen"] bridge:self.bridge];

    UIViewController <NNView> *rootController = (UIViewController <NNView> *) [UIApplication sharedApplication].keyWindow.rootViewController;

    NNSingleNode *singleNode = self.node;
    singleNode.modal = nodeObject;

    NSDictionary *newState = rootController.node.data;
    [RNNNState sharedInstance].state = newState;
    callback(@[newState]);

    dispatch_async(dispatch_get_main_queue(), ^{
        UIViewController *viewController = [nodeObject generate];
        if (viewController) {
            [self presentViewController:viewController animated:YES completion:nil];
        }
    });
}

@end
