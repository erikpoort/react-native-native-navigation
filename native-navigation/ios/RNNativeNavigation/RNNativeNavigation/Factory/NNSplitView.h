//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NNView.h"

@class NNSplitNode;


@interface NNSplitView : UIViewController <NNView>

- (instancetype)initWithNode:(NNSplitNode *)node;

@end
