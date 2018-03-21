//
// Created by Juan Curti on 1/4/18.
// Copyright (c) 2018 Facebook. All rights reserved.
//

#import <React/RCTBridge.h>
#import <React/RCTRootView.h>
#import "ExampleView.h"
#import "ExampleNode.h"


@interface ExampleView ()

@property (nonatomic, strong) ExampleNode *exampleNode;
@property (nonatomic, strong) RCTBridge *bridge;

@end


@implementation ExampleView

- (instancetype)initWithBridge:(RCTBridge *)bridge node:(ExampleNode *)node
{
    if (self = [super init]) {
        self.exampleNode = node;
        self.bridge = bridge;
    }

    return self;
}

- (__kindof id<NNNode>)node
{
    return self.exampleNode;
}

- (void)loadView
{
    self.view = [[RCTRootView alloc] initWithBridge:self.bridge moduleName:self.node.screenID initialProperties:nil];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewWillAppear:animated];

    self.view.backgroundColor = UIColor.redColor;
}

- (UIViewController<NNView> *)viewForPath:(NSString *)path
{
    UIViewController<NNView> *modalController = (UIViewController<NNView> *)self.presentedViewController;
    if (modalController && [path rangeOfString:modalController.node.screenID].location == 0) {
        if (![modalController.node.screenID isEqualToString:path]) {
            return [modalController viewForPath:path];
        } else {
            return modalController;
        }
    }
    return self;
}

@end
