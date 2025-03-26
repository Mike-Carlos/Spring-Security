package com.activity.secure.Service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * This class provides services for managing a blacklist of tokens.
 */
@Service
public class TokenBlacklistService {
    private final Set<String> blacklistedTokens = new HashSet<>();

    /**
     * Adds a token to the blacklist.
     *
     * @param token the token to blacklist
     */
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    /**
     * Checks if a token is blacklisted.
     *
     * @param token the token to check
     * @return true if the token is blacklisted, false otherwise
     */
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}