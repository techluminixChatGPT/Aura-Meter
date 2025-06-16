package com.aurameter.backend.repository;

import com.aurameter.backend.model.EmailVerificationToken;
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
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    
    // Find token by token string
    Optional<EmailVerificationToken> findByToken(String token);
    
    // Find valid token by token string
    @Query("SELECT t FROM EmailVerificationToken t WHERE t.token = :token AND t.isUsed = false AND t.expiresAt > :now")
    Optional<EmailVerificationToken> findValidToken(@Param("token") String token, @Param("now") LocalDateTime now);
    
    // Find tokens by user
    List<EmailVerificationToken> findByUser(User user);
    
    // Find unused tokens by user
    List<EmailVerificationToken> findByUserAndIsUsedFalse(User user);
    
    // Delete expired tokens
    @Modifying
    @Query("DELETE FROM EmailVerificationToken t WHERE t.expiresAt < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
    
    // Mark all user tokens as used (when user verifies)
    @Modifying
    @Query("UPDATE EmailVerificationToken t SET t.isUsed = true, t.usedAt = :now WHERE t.user = :user AND t.isUsed = false")
    void markAllUserTokensAsUsed(@Param("user") User user, @Param("now") LocalDateTime now);
}