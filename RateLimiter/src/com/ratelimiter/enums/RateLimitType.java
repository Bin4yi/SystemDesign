package com.ratelimiter.enums;

public enum RateLimitType {
    TOKEN_BUCKET,
    FIXED_WINDOW,
    SLIDING_WINDOW_LOG,
    SLIDING_WINDOW_COUNTER
}
