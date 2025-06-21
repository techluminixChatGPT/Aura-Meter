package com.aurameter.backend.exception;

public class InvalidCredentialsException extends RuntimeException {
    
    private final String attemptedUsername;
    private final String ipAddress;
    
    public InvalidCredentialsException(String message) {
        super(message);
        this.attemptedUsername = null;
        this.ipAddress = null;
    }
    
    public InvalidCredentialsException(String message, String attemptedUsername, String ipAddress) {
        super(message);
        this.attemptedUsername = attemptedUsername;
        this.ipAddress = ipAddress;
    }
    
    public String getAttemptedUsername() { return attemptedUsername; }
    public String getIpAddress() { return ipAddress; }
}