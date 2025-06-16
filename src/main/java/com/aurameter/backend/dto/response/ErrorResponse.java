package com.aurameter.backend.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
    
    private Boolean success = false;
    private String message;
    private String error;
    private Integer status;
    private String path;
    private List<String> details;
    private LocalDateTime timestamp;
    
    // Constructors
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ErrorResponse(String message, String error, Integer status, String path) {
        this.message = message;
        this.error = error;
        this.status = status;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
    
    public ErrorResponse(String message, String error, Integer status, String path, List<String> details) {
        this.message = message;
        this.error = error;
        this.status = status;
        this.path = path;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    
    public List<String> getDetails() { return details; }
    public void setDetails(List<String> details) { this.details = details; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}