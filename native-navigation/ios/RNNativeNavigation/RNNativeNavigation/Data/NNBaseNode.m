//
// Copyright (c) 2017 MediaMonks. All rights reserved.
//

#import "NNBaseNode.h"

NSString *const kScreenId = @"screenID";
NSString *const kType = @"type";


@interface NNBaseNode ()

@property (nonatomic, copy) NSString *screenID;
@property (nonatomic, copy) NSString *type;

@end


@implementation NNBaseNode

- (void)setData:(NSDictionary *)data
{
    self.screenID = data[kScreenId];
    self.type = data[kType];
}

- (NSDictionary *)data
{
    return @{
        kScreenId : self.screenID,
        kType : self.type,
    };
}

+ (NSDictionary<NSString *, id> *)constantsToExport
{
    return @{};
}

+ (NSString *)jsName
{
    NSAssert(NO, @"%@ should be overridden", NSStringFromSelector(_cmd));
    return nil;
}

- (void)setBridge:(RCTBridge *)bridge
{
    NSAssert(NO, @"%@ should be overridden", NSStringFromSelector(_cmd));
}

- (NSString *)title
{
    NSAssert(NO, @"%@ should be overridden", NSStringFromSelector(_cmd));
    return nil;
}

- (UIViewController<NNView> *)generate
{
    NSAssert(NO, @"%@ should be overridden", NSStringFromSelector(_cmd));
    return nil;
}

@end
