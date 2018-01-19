//

// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol NNNode;

@protocol NNView <NSObject>

@required
- (__kindof id <NNNode>)node;

- (UIViewController <NNView> *)viewForPath:(NSString *)path;

@optional
- (void)callMethodWithName:(NSString *)methodName arguments:(NSDictionary *)arguments callback:(void (^)(NSArray *))callback;

@end
