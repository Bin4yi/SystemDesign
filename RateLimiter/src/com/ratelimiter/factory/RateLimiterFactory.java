package com.ratelimiter.factory;

import com.ratelimiter.enums.RateLimitType;
import com.ratelimiter.limiter.FixedWindowRateLimiter;
import com.ratelimiter.limiter.RateLimiter;
import com.ratelimiter.limiter.TokenBucketRateLimiter;
import com.ratelimiter.model.RateLimitConfig;

public class RateLimiterFactory {
    private RateLimiterFactory() {}

    public static RateLimiter createRateLimiter(RateLimitType type, RateLimitConfig config) {
        switch (type) {
            case TOKEN_BUCKET:
                return new TokenBucketRateLimiter(config);
            case FIXED_WINDOW:
                return new FixedWindowRateLimiter(config);
            case SLIDING_WINDOW_LOG:
            case SLIDING_WINDOW_COUNTER:
                throw new UnsupportedOperationException(type + " is not implemented yet");
            default:
                throw new IllegalArgumentException("Unknown rate limiter type: " + type);
        }
    }
}
