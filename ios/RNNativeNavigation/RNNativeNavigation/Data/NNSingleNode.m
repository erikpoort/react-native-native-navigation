//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <React/RCTBridge.h>
#import "NNSingleNode.h"
#import "NNSingleView.h"
#import "NNNodeHelper.h"

static NSString *const kModal = @"modal";


@interface NNSingleNode ()

@property (nonatomic, strong) RCTBridge *bridge;

@end


@implementation NNSingleNode

+ (NSString *)jsName
{
    return @"SingleView";
}

- (UIViewController<NNView> *)generate
{
    return [[NNSingleView alloc] initWithBridge:self.bridge node:self];
}

- (void)setData:(NSDictionary *)data
{
    [super setData:data];
    self.modal = [NNNodeHelper.sharedInstance nodeFromMap:data[kModal] bridge:self.bridge];
}

- (NSDictionary *)data
{
    NSMutableDictionary *data = [super data].mutableCopy;
    if (self.modal) {
        data[kModal] = self.modal.data;
    }
    return data.copy;
}

+ (NSDictionary<NSString *, id> *)constantsToExport
{
    return @{
        kShowModal : kShowModal
    };
}

@end
