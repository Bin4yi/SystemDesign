package com.ratelimiter;

import com.ratelimiter.enums.UserTier;
import com.ratelimiter.model.User;
import com.ratelimiter.service.RateLimiterService;

public class Main {
    public static void main(String[] args) {
        System.out.println("Rate Limiter Application Started");

        RateLimiterService service = new RateLimiterService();
        User freeUser = new User("free-1", UserTier.FREE);
        User premiumUser = new User("premium-1", UserTier.PREMIUM);

        System.out.println("--- FREE user (token bucket, 5 per 60s) ---");
        for (int i = 1; i <= 7; i++) {
            System.out.println("Request " + i + " allowed: " + service.allowRequest(freeUser));
        }

        System.out.println("--- PREMIUM user (fixed window, 100 per 60s) ---");
        for (int i = 1; i <= 3; i++) {
            System.out.println("Request " + i + " allowed: " + service.allowRequest(premiumUser));
        }

        System.out.println("--- FREE user is limited independently of PREMIUM ---");
        System.out.println("Another FREE user allowed: " + service.allowRequest(new User("free-2", UserTier.FREE)));
    }
}
