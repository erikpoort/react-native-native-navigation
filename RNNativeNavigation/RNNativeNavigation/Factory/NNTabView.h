//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol NNNode;

@interface NNTabView : UIViewController

- (instancetype)initWithTabs:(NSArray <id <NNNode>> *)tabs selectedTab:(NSUInteger)tab;

@end
