//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol NNNode;

@interface NNStackView : UINavigationController

- (instancetype)initWithStack:(NSArray <id <NNNode>> *)stack;

@end
