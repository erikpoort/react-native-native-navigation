//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <React/RCTBridge.h>
#import "NNSingleNode.h"
#import "NNSingleView.h"
#import "NNNodeHelper.h"

static NSString *const kPage = @"page";
static NSString *const kModal = @"modal";


@interface NNSingleNode ()

@property (nonatomic, strong) RCTBridge *bridge;
@property (nonatomic, copy) NSString *page;

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
    self.page = data[kPage];
    self.modal = [NNNodeHelper.sharedInstance nodeFromMap:data[kModal] bridge:self.bridge];
}

- (NSDictionary *)data
{
    NSMutableDictionary *data = [super data].mutableCopy;
    data[kPage] = self.page;
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
