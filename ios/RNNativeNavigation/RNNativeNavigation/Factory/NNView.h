//

// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol NNNode;

@protocol NNView <NSObject>

- (id <NNNode>)node;
- (UIViewController <NNView> *)viewForPath:(NSString *)path;

@end
