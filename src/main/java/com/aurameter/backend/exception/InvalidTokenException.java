package com.aurameter.backend.exception;

public class InvalidTokenException extends RuntimeException {
    
    private final String tokenType;
    private final String reason;
    
    public InvalidTokenException(String message) {
        super(message);
        this.tokenType = null;
        this.reason = null;
    }
    
    public InvalidTokenException(String tokenType, String reason) {
        super(String.format("Invalid %s token: %s", tokenType, reason));
        this.tokenType = tokenType;
        this.reason = reason;
    }
    
    public String getTokenType() { return tokenType; }
    public String getReason() { return reason; }
}