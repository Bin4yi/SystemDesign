package com.ratelimiter.limiter;

import com.ratelimiter.enums.RateLimitType;
import com.ratelimiter.model.RateLimitConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FixedWindowRateLimiter extends RateLimiter {
    private static class Window {
        long windowStart;
        int count;
    }

    private final Map<String, Window> windows = new ConcurrentHashMap<>();

    public FixedWindowRateLimiter(RateLimitConfig config) {
        super(config, RateLimitType.FIXED_WINDOW);
    }

    @Override
    public boolean allowRequest(String userId) {
        long now = System.currentTimeMillis();
        long windowMs = config.getWindowInSeconds() * 1000L;
        long currentWindowStart = (now / windowMs) * windowMs;
        Window window = windows.computeIfAbsent(userId, k -> new Window());

        synchronized (window) {
            if (window.windowStart != currentWindowStart) {
                window.windowStart = currentWindowStart;
                window.count = 0;
            }
            if (window.count < config.getMaxRequests()) {
                window.count++;
                return true;
            }
            return false;
        }
    }
}
