//
// Copyright (c) 2018 MediaMonks. All rights reserved.
//

#import "ReactNativeNativeEventEmitter.h"
#import "NNView.h"
#import "NNNode.h"

@implementation ReactNativeNativeEventEmitter
RCT_EXPORT_MODULE();

- (NSArray<NSString *> *)supportedEvents {
    UIViewController <NNView> *rootController = (UIViewController <NNView> *) [UIApplication sharedApplication].keyWindow.rootViewController;
    return rootController.node.supportedEvents;
}

@end
