//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <UIKit/UIKit.h>

@class NNSingleNode;

@interface NNSingleView : UIViewController

- (instancetype)initWithBridge:(RCTBridge *)bridge node:(NNSingleNode *)node;

@end
