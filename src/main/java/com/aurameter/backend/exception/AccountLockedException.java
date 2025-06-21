package com.aurameter.backend.exception;

import java.time.LocalDateTime;

public class AccountLockedException extends RuntimeException {
    
    private final String username;
    private final LocalDateTime lockedAt;
    private final String reason;
    
    public AccountLockedException(String username, LocalDateTime lockedAt, String reason) {
        super(String.format("Account '%s' is locked. Reason: %s", username, reason));
        this.username = username;
        this.lockedAt = lockedAt;
        this.reason = reason;
    }
    
    public String getUsername() { return username; }
    public LocalDateTime getLockedAt() { return lockedAt; }
    public String getReason() { return reason; }
}