package com.aurameter.backend.repository;

import com.aurameter.backend.model.UserActivityLog;
import com.aurameter.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long> {
    
    // Find user's activity logs
    List<UserActivityLog> findByUser(User user);
    
    // Find user's activity logs with pagination
    Page<UserActivityLog> findByUser(User user, Pageable pageable);
    
    // Find user's activity by type
    List<UserActivityLog> findByUserAndActivityType(User user, String activityType);
    
    // Find recent user activity
    @Query("SELECT a FROM UserActivityLog a WHERE a.user = :user AND a.createdAt >= :since ORDER BY a.createdAt DESC")
    List<UserActivityLog> findRecentUserActivity(@Param("user") User user, @Param("since") LocalDateTime since);
    
    // Find failed login attempts for user
    @Query("SELECT a FROM UserActivityLog a WHERE a.user = :user AND a.activityType = 'LOGIN' AND a.success = false AND a.createdAt >= :since")
    List<UserActivityLog> findFailedLoginAttempts(@Param("user") User user, @Param("since") LocalDateTime since);
    
    // Find suspicious activity (multiple IPs)
    @Query("SELECT a FROM UserActivityLog a WHERE a.user = :user AND a.activityType = 'LOGIN' AND a.success = true AND a.createdAt >= :since GROUP BY a.ipAddress HAVING COUNT(DISTINCT a.ipAddress) > 1")
    List<UserActivityLog> findSuspiciousActivity(@Param("user") User user, @Param("since") LocalDateTime since);
    
    // Count activities by type and date range
    @Query("SELECT COUNT(a) FROM UserActivityLog a WHERE a.activityType = :activityType AND a.createdAt BETWEEN :startDate AND :endDate")
    long countByActivityTypeAndDateRange(@Param("activityType") String activityType, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Find activities by IP address
    List<UserActivityLog> findByIpAddress(String ipAddress);
    
    // Delete old activity logs (for cleanup)
    @Modifying
    @Query("DELETE FROM UserActivityLog a WHERE a.createdAt < :cutoffDate")
    void deleteOldActivityLogs(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    // Get daily activity stats
    @Query("SELECT DATE(a.createdAt) as date, a.activityType, COUNT(a) as count FROM UserActivityLog a WHERE a.createdAt >= :since GROUP BY DATE(a.createdAt), a.activityType ORDER BY date DESC")
    List<Object[]> getDailyActivityStats(@Param("since") LocalDateTime since);
}