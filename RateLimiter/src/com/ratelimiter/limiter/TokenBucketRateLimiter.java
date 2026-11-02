package com.ratelimiter.limiter;

import com.ratelimiter.enums.RateLimitType;
import com.ratelimiter.model.RateLimitConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenBucketRateLimiter extends RateLimiter {

    private static class Bucket {
        double tokens;
        long lastRefillTime;

        Bucket(double tokens, long lastRefillTime) {
            this.tokens = tokens;
            this.lastRefillTime = lastRefillTime;
        }
    }

    private final Map<String, Bucket> userBuckets = new ConcurrentHashMap<>();

    public TokenBucketRateLimiter(RateLimitConfig config) {
        super(config, RateLimitType.TOKEN_BUCKET);
    }

    @Override
    public boolean allowRequest(String userId) {
        long now = System.currentTimeMillis();
        Bucket bucket = userBuckets.computeIfAbsent(userId, k -> new Bucket(config.getMaxRequests(), now));

        synchronized (bucket) {
            refillTokens(bucket, now);
            if (bucket.tokens >= 1) {
                bucket.tokens -= 1;
                return true;
            }
            return false;
        }
    }

    private void refillTokens(Bucket bucket, long now) {
        double elapsedSeconds = (now - bucket.lastRefillTime) / 1000.0;
        double refillRate = (double) config.getMaxRequests() / config.getWindowInSeconds();
        bucket.tokens = Math.min(config.getMaxRequests(), bucket.tokens + elapsedSeconds * refillRate);
        bucket.lastRefillTime = now;
    }
}
