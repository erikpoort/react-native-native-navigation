//
// Created by Juan Curti on 1/4/18.
// Copyright (c) 2018 Facebook. All rights reserved.
//

#import "ExampleNode.h"
#import "ExampleView.h"

static NSString *const kName = @"name";
static NSString *const kLazyLoad = @"lazyLoad";

@interface ExampleNode()
    @property (nonatomic, strong) RCTBridge *bridge;
    @property (nonatomic, copy) NSString *title;
@end


@implementation ExampleNode

+ (NSString *)jsName {
    return @"ExampleView";
}

- (UIViewController <NNView> *)generate {
    return [[ExampleView alloc] initWithBridge:self.bridge node:self];
}

- (void)setData:(NSDictionary *)data {
    [super setData:data];
    self.title = data[kName];
    self.lazyLoad = [data[kLazyLoad] isEqualToString:@"true"];
}

- (NSDictionary *)data {
    NSMutableDictionary *data = [super data].mutableCopy;
    data[kName] = self.title;
    data[kLazyLoad] = self.lazyLoad ? @"true" : @"false";
    return data.copy;
}

@end