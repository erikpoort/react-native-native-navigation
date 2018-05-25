//
// Copyright (c) 2018 MediaMonks. All rights reserved.
//

#import "ReactNativeNativeEventEmitter.h"
#import "NNView.h"
#import "NNNode.h"
#import "RNNNState.h"

@implementation ReactNativeNativeEventEmitter
RCT_EXPORT_MODULE();

- (NSArray<NSString *> *)supportedEvents {
    UIViewController <NNView> *rootController = RNNNState.sharedInstance.viewController;
    return rootController.node.supportedEvents;
}

@end
