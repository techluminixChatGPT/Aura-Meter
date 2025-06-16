package com.aurameter.backend.repository;

import com.aurameter.backend.model.PasswordResetToken;
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
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    
    // Find token by token string
    Optional<PasswordResetToken> findByToken(String token);
    
    // Find valid token by token string
    @Query("SELECT t FROM PasswordResetToken t WHERE t.token = :token AND t.isUsed = false AND t.expiresAt > :now")
    Optional<PasswordResetToken> findValidToken(@Param("token") String token, @Param("now") LocalDateTime now);
    
    // Find tokens by user
    List<PasswordResetToken> findByUser(User user);
    
    // Find unused tokens by user
    List<PasswordResetToken> findByUserAndIsUsedFalse(User user);
    
    // Delete expired tokens
    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.expiresAt < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
    
    // Invalidate all user tokens (when password is reset)
    @Modifying
    @Query("UPDATE PasswordResetToken t SET t.isUsed = true, t.usedAt = :now WHERE t.user = :user AND t.isUsed = false")
    void invalidateAllUserTokens(@Param("user") User user, @Param("now") LocalDateTime now);
}