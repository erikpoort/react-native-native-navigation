//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NNView.h"
#import "MMDrawerController.h"
#import "NNDrawerNode.h"

@class NNDrawerNode;

@interface NNDrawerView : MMDrawerController <NNView>

- (instancetype)initWithNode:(NNDrawerNode *)node;
- (NNDrawerSide)sideForPath:(NSString *)path;


@end
