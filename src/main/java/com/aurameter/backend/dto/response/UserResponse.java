package com.aurameter.backend.dto.response;

import java.time.LocalDateTime;

public class UserResponse {
    
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String profileImageUrl;
    private Boolean isEmailVerified;
    private Long totalAuraPoints;
    private Integer currentStreak;
    private Integer longestStreak;
    private LocalDateTime createdAt;
    
    // Constructors
    public UserResponse() {}
    
    public UserResponse(Long id, String username, String email, String fullName, 
                       Boolean isEmailVerified, Long totalAuraPoints, Integer currentStreak, 
                       Integer longestStreak, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.isEmailVerified = isEmailVerified;
        this.totalAuraPoints = totalAuraPoints;
        this.currentStreak = currentStreak;
        this.longestStreak = longestStreak;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
    
    public Boolean getIsEmailVerified() { return isEmailVerified; }
    public void setIsEmailVerified(Boolean isEmailVerified) { this.isEmailVerified = isEmailVerified; }
    
    public Long getTotalAuraPoints() { return totalAuraPoints; }
    public void setTotalAuraPoints(Long totalAuraPoints) { this.totalAuraPoints = totalAuraPoints; }
    
    public Integer getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(Integer currentStreak) { this.currentStreak = currentStreak; }
    
    public Integer getLongestStreak() { return longestStreak; }
    public void setLongestStreak(Integer longestStreak) { this.longestStreak = longestStreak; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}