package com.aurameter.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_activity_log")
public class UserActivityLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(nullable = false, length = 50)
    private String activityType; // 'LOGIN', 'LOGOUT', 'REGISTER', 'PASSWORD_RESET', etc.
    
    @Column(length = 45) // IPv6 support
    private String ipAddress;
    
    @Column(columnDefinition = "TEXT")
    private String userAgent;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean success = true;
    
    private String failureReason; // If success = FALSE
    
    // @Column(columnDefinition = "jsonb")
    // private String additionalData; // Flexible JSON data storage
    
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    // Constructors
    public UserActivityLog() {}
    
    public UserActivityLog(User user, String activityType, String ipAddress, Boolean success) {
        this.user = user;
        this.activityType = activityType;
        this.ipAddress = ipAddress;
        this.success = success;
        this.createdAt = LocalDateTime.now();
    }
    
    public UserActivityLog(String activityType, String ipAddress, Boolean success) {
        this.activityType = activityType;
        this.ipAddress = ipAddress;
        this.success = success;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getActivityType() { return activityType; }
    public void setActivityType(String activityType) { this.activityType = activityType; }
    
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    
    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }
    
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
    
    // public String getAdditionalData() { return additionalData; }
    // public void setAdditionalData(String additionalData) { this.additionalData = additionalData; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}