package com.aurameter.backend.repository;

import com.aurameter.backend.model.RefreshToken;
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
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    
    // Find token by hash
    Optional<RefreshToken> findByTokenHash(String tokenHash);
    
    // Find valid token by hash
    @Query("SELECT t FROM RefreshToken t WHERE t.tokenHash = :tokenHash AND t.isRevoked = false AND t.expiresAt > :now")
    Optional<RefreshToken> findValidToken(@Param("tokenHash") String tokenHash, @Param("now") LocalDateTime now);
    
    // Find by token hash and not revoked
    Optional<RefreshToken> findByTokenHashAndIsRevokedFalse(String tokenHash);
    
    // Find all user's active tokens
    List<RefreshToken> findByUserAndIsRevokedFalse(User user);
    
    // Find all user's tokens
    List<RefreshToken> findByUser(User user);
    
    // Revoke all user tokens (when password reset or logout from all devices)
    @Modifying
    @Query("UPDATE RefreshToken t SET t.isRevoked = true WHERE t.user = :user")
    void revokeAllUserTokens(@Param("user") User user);
    
    // Revoke specific device tokens
    @Modifying
    @Query("UPDATE RefreshToken t SET t.isRevoked = true WHERE t.user = :user AND t.deviceInfo LIKE %:deviceInfo%")
    void revokeUserDeviceTokens(@Param("user") User user, @Param("deviceInfo") String deviceInfo);
    
    // Delete expired tokens
    @Modifying
    @Query("DELETE FROM RefreshToken t WHERE t.expiresAt < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
    
    // Delete revoked tokens older than X days
    @Modifying
    @Query("DELETE FROM RefreshToken t WHERE t.isRevoked = true AND t.createdAt < :cutoffDate")
    void deleteOldRevokedTokens(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    // Count active tokens for user
    long countByUserAndIsRevokedFalse(User user);
}