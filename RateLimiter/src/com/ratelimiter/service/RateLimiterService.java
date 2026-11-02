package com.ratelimiter.service;

import com.ratelimiter.enums.RateLimitType;
import com.ratelimiter.enums.UserTier;
import com.ratelimiter.factory.RateLimiterFactory;
import com.ratelimiter.limiter.RateLimiter;
import com.ratelimiter.model.RateLimitConfig;
import com.ratelimiter.model.User;

import java.util.HashMap;
import java.util.Map;

public class RateLimiterService {
    private final Map<UserTier, RateLimiter> rateLimiters = new HashMap<>();

    public RateLimiterService() {
        rateLimiters.put(
                UserTier.FREE,
                RateLimiterFactory.createRateLimiter(
                        RateLimitType.TOKEN_BUCKET,
                        new RateLimitConfig(
                                5, // max requests
                                60 // window in seconds
                        )
                )
        );

        rateLimiters.put(
                UserTier.PREMIUM,
                RateLimiterFactory.createRateLimiter(
                        RateLimitType.FIXED_WINDOW,
                        new RateLimitConfig(
                                100, // max requests
                                60 // window in seconds
                        )
                )
        );
    }

    public boolean allowRequest(User user) {
        RateLimiter limiter = rateLimiters.get(user.getUserTier());
        if (limiter == null) {
            throw new IllegalArgumentException("No rate limiter configured for user tier: " + user.getUserTier());
        }
        return limiter.allowRequest(user.getUserId());
    }
}
