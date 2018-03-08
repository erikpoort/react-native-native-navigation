//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNDrawerNode.h"
#import "NNNodeHelper.h"
#import "NNDrawerView.h"

@interface NNDrawerNode ()

@property(nonatomic, strong) RCTBridge *bridge;

@end

@implementation NNDrawerNode

+ (NSString *)jsName {
    return @"DrawerView";
}

- (UIViewController <NNView> *)generate {
    return [[NNDrawerView alloc] initWithNode:self];
}

- (void)setData:(NSDictionary *)data {
    [super setData:data];

    self.leftNode = [NNNodeHelper.sharedInstance nodeFromMap:data[LEFT] bridge:self.bridge];
    self.centerNode = [NNNodeHelper.sharedInstance nodeFromMap:data[CENTER] bridge:self.bridge];
    self.rightNode = [NNNodeHelper.sharedInstance nodeFromMap:data[RIGHT] bridge:self.bridge];

    NSArray *sides = @[@"center", @"left", @"right"];
    NSString *side = data[SIDE];
    self.side = (MMDrawerSide) (side ? [sides indexOfObject:side] : 0);
}

- (NSDictionary *)data {
    NSMutableDictionary *data = [super data].mutableCopy;
    if (self.leftNode) {
        data[LEFT] = self.leftNode.data;
    }
    if (self.centerNode) {
        data[CENTER] = self.centerNode.data;
    }
    if (self.rightNode) {
        data[RIGHT] = self.rightNode.data;
    }
    NSArray *sides = @[CENTER, LEFT, RIGHT];
    data[SIDE] = sides[self.side];
    return data.copy;
}

- (NSString *)title {
    return self.centerNode.title;
}

+ (NSDictionary<NSString *, id> *)constantsToExport {
    return @{
            kOpenView: kOpenView,
    };
}

@end
