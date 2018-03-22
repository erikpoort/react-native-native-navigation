//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import <React/RCTBridge.h>
#import "NNSingleNode.h"
#import "NNSingleView.h"
#import "NNNodeHelper.h"

static NSString *const kPage = @"page";
static NSString *const kModal = @"modal";
static NSString *const kStyle = @"style";


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
    self.style = data[kStyle];
}

- (NSDictionary *)data
{
    NSMutableDictionary *data = [super data].mutableCopy;
    data[kPage] = self.page;
    data[kStyle] = self.style;
    if (self.modal) {
        data[kModal] = self.modal.data;
    }
    return data.copy;
}

+ (NSDictionary<NSString *, id> *)constantsToExport
{
    return @{
        kShowModal: kShowModal,
        kChangeTitle: kChangeTitle
    };
}

@end
