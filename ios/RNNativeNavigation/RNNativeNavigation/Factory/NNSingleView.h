//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NNView.h"

@class NNSingleNode;

@interface NNSingleView : UIViewController <NNView>

- (instancetype)initWithBridge:(RCTBridge *)bridge node:(NNSingleNode *)node;

@end
