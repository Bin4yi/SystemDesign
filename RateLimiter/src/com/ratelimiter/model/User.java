package com.ratelimiter.model;

import com.ratelimiter.enums.UserTier;

public class User {
    private String userId;
    private UserTier userTier;

    public User(String userId, UserTier userTier) {
        this.userId = userId;
        this.userTier = userTier;
    }

    public String getUserId() {
        return userId;
    }

    public UserTier getUserTier() {
        return userTier;
    }
}
