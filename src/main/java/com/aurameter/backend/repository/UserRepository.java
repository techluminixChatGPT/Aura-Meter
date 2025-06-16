package com.aurameter.backend.repository;

import com.aurameter.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Find user by email
    Optional<User> findByEmail(String email);
    
    // Find user by username
    Optional<User> findByUsername(String username);
    
    // Find user by email OR username (for login)
    Optional<User> findByEmailOrUsername(String email, String username);
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Check if username exists
    boolean existsByUsername(String username);
    
    // Find unverified users older than certain date
    @Query("SELECT u FROM User u WHERE u.isEmailVerified = false AND u.createdAt < :cutoffDate")
    List<User> findUnverifiedUsersOlderThan(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    // Reset failed login attempts
    @Modifying
    @Query("UPDATE User u SET u.failedLoginAttempts = 0 WHERE u.id = :userId")
    void resetFailedLoginAttempts(@Param("userId") Long userId);
    
    // Lock user account
    @Modifying
    @Query("UPDATE User u SET u.accountLocked = true WHERE u.id = :userId")
    void lockUserAccount(@Param("userId") Long userId);
    
    // Find top users by aura points (for leaderboard)
    @Query("SELECT u FROM User u WHERE u.isActive = true ORDER BY u.totalAuraPoints DESC")
    List<User> findTopUsersByAuraPoints();
    
    // Count active users
    long countByIsActiveTrue();
    
    // Count verified users
    long countByIsEmailVerifiedTrue();
}