package com.aurameter.backend.exception;

public class UserNotFoundException extends RuntimeException {
    
    private final String field;
    private final String value;
    
    public UserNotFoundException(String message) {
        super(message);
        this.field = null;
        this.value = null;
    }
    
    public UserNotFoundException(String field, String value) {
        super(String.format("User not found with %s: %s", field, value));
        this.field = field;
        this.value = value;
    }
    
    public UserNotFoundException(String message, String field, String value) {
        super(message);
        this.field = field;
        this.value = value;
    }
    
    public String getField() { return field; }
    public String getValue() { return value; }
}