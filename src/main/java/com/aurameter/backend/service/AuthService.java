// package com.aurameter.backend.service;

// import com.aurameter.backend.dto.request.*;
// import com.aurameter.backend.dto.response.*;
// import com.aurameter.backend.model.*;
// import com.aurameter.backend.repository.*;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.time.LocalDateTime;
// import java.util.Optional;
// import java.util.UUID;

// @Service
// @Transactional
// public class AuthService {
    
//     private final UserRepository userRepository;
//     private final EmailVerificationTokenRepository emailVerificationTokenRepository;
//     private final PasswordResetTokenRepository passwordResetTokenRepository;
//     private final RefreshTokenRepository refreshTokenRepository;
//     private final UserActivityLogRepository userActivityLogRepository;
//     private final PasswordEncoder passwordEncoder;
//     private final JwtService jwtService;
//     private final EmailService emailService;
    
//     @Autowired
//     public AuthService(UserRepository userRepository,
//                       EmailVerificationTokenRepository emailVerificationTokenRepository,
//                       PasswordResetTokenRepository passwordResetTokenRepository,
//                       RefreshTokenRepository refreshTokenRepository,
//                       UserActivityLogRepository userActivityLogRepository,
//                       PasswordEncoder passwordEncoder,
//                       JwtService jwtService,
//                       EmailService emailService) {
//         this.userRepository = userRepository;
//         this.emailVerificationTokenRepository = emailVerificationTokenRepository;
//         this.passwordResetTokenRepository = passwordResetTokenRepository;
//         this.refreshTokenRepository = refreshTokenRepository;
//         this.userActivityLogRepository = userActivityLogRepository;
//         this.passwordEncoder = passwordEncoder;
//         this.jwtService = jwtService;
//         this.emailService = emailService;
//     }
    
//     // Register new user
//     public ApiResponse registerUser(RegisterRequest request, String ipAddress) {
//         // 1. Check if email already exists
//         if (userRepository.existsByEmail(request.getEmail())) {
//             return ApiResponse.error("Email already exists");
//         }
        
//         // 2. Check if username already exists
//         if (userRepository.existsByUsername(request.getUsername())) {
//             return ApiResponse.error("Username already exists");
//         }
        
//         // 3. Hash password
//         String hashedPassword = passwordEncoder.encode(request.getPassword());
        
//         // 4. Create user entity
//         User user = new User(
//             request.getUsername(),
//             request.getEmail(),
//             hashedPassword,
//             request.getFullName()
//         );
        
//         // 5. Save user to database
//         User savedUser = userRepository.save(user);
        
//         // 6. Generate email verification token
//         String verificationToken = UUID.randomUUID().toString();
//         EmailVerificationToken tokenEntity = new EmailVerificationToken(
//             savedUser,
//             verificationToken,
//             LocalDateTime.now().plusHours(24) // 24 hours expiry
//         );
//         emailVerificationTokenRepository.save(tokenEntity);
        
//         // 7. Send verification email
//         emailService.sendVerificationEmail(savedUser.getEmail(), verificationToken);
        
//         // 8. Log registration activity
//         UserActivityLog activityLog = new UserActivityLog(
//             savedUser,
//             "REGISTER",
//             ipAddress,
//             true
//         );
//         userActivityLogRepository.save(activityLog);
        
//         return ApiResponse.success(
//             "Registration successful! Please check your email to verify your account."
//         );
//     }
    
//     // Login user
//     public AuthResponse loginUser(LoginRequest request, String ipAddress, String userAgent) {
//         // 1. Find user by email or username
//         Optional<User> userOptional = userRepository.findByEmailOrUsername(
//             request.getEmailOrUsername(),
//             request.getEmailOrUsername()
//         );
        
//         if (userOptional.isEmpty()) {
//             logFailedLogin(null, "USER_NOT_FOUND", ipAddress, userAgent);
//             throw new RuntimeException("Invalid credentials");
//         }
        
//         User user = userOptional.get();
        
//         // 2. Check if account is locked
//         if (user.getAccountLocked()) {
//             logFailedLogin(user, "ACCOUNT_LOCKED", ipAddress, userAgent);
//             throw new RuntimeException("Account is locked due to multiple failed login attempts");
//         }
        
//         // 3. Check if email is verified
//         if (!user.getIsEmailVerified()) {
//             logFailedLogin(user, "EMAIL_NOT_VERIFIED", ipAddress, userAgent);
//             throw new RuntimeException("Please verify your email before logging in");
//         }
        
//         // 4. Verify password
//         if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
//             handleFailedLogin(user, ipAddress, userAgent);
//             throw new RuntimeException("Invalid credentials");
//         }
        
//         // 5. Reset failed login attempts and update login info
//         user.setFailedLoginAttempts(0);
//         user.setLastLoginAt(LocalDateTime.now());
//         user.setLastLoginIp(ipAddress);
//         userRepository.save(user);
        
//         // 6. Generate JWT tokens
//         String accessToken = jwtService.generateAccessToken(user);
//         String refreshToken = jwtService.generateRefreshToken(user);
        
//         // 7. Save refresh token
//         RefreshToken refreshTokenEntity = new RefreshToken(
//             user,
//             jwtService.hashToken(refreshToken),
//             userAgent,
//             ipAddress,
//             LocalDateTime.now().plusDays(30) // 30 days expiry
//         );
//         refreshTokenRepository.save(refreshTokenEntity);
        
//         // 8. Log successful login
//         UserActivityLog loginLog = new UserActivityLog(
//             user,
//             "LOGIN",
//             ipAddress,
//             true
//         );
//         loginLog.setUserAgent(userAgent);
//         userActivityLogRepository.save(loginLog);
        
//         // 9. Create user response
//         UserResponse userResponse = createUserResponse(user);
        
//         return new AuthResponse(
//             accessToken,
//             refreshToken,
//             jwtService.getAccessTokenExpiry(),
//             userResponse,
//             "Login successful"
//         );
//     }
    
//     // Verify email
//     public ApiResponse verifyEmail(String token) {
//         // 1. Find valid token
//         Optional<EmailVerificationToken> tokenOptional = emailVerificationTokenRepository
//             .findValidToken(token, LocalDateTime.now());
        
//         if (tokenOptional.isEmpty()) {
//             return ApiResponse.error("Invalid or expired verification token");
//         }
        
//         EmailVerificationToken verificationToken = tokenOptional.get();
//         User user = verificationToken.getUser();
        
//         // 2. Mark user as verified
//         user.setIsEmailVerified(true);
//         userRepository.save(user);
        
//         // 3. Mark token as used
//         verificationToken.setIsUsed(true);
//         verificationToken.setUsedAt(LocalDateTime.now());
//         emailVerificationTokenRepository.save(verificationToken);
        
//         // 4. Log verification activity
//         UserActivityLog activityLog = new UserActivityLog(
//             user,
//             "EMAIL_VERIFIED",
//             null,
//             true
//         );
//         userActivityLogRepository.save(activityLog);
        
//         return ApiResponse.success("Email verified successfully! You can now log in.");
//     }
    
//     // Helper method to handle failed login
//     private void handleFailedLogin(User user, String ipAddress, String userAgent) {
//         user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
        
//         // Lock account after 5 failed attempts
//         if (user.getFailedLoginAttempts() >= 5) {
//             user.setAccountLocked(true);
//             userRepository.save(user);
            
//             // Log account lock
//             UserActivityLog lockLog = new UserActivityLog(
//                 user,
//                 "ACCOUNT_LOCKED",
//                 ipAddress,
//                 true
//             );
//             lockLog.setUserAgent(userAgent);
//             userActivityLogRepository.save(lockLog);
//         } else {
//             userRepository.save(user);
//         }
        
//         logFailedLogin(user, "INVALID_PASSWORD", ipAddress, userAgent);
//     }
    
//     // Helper method to log failed login attempts
//     private void logFailedLogin(User user, String reason, String ipAddress, String userAgent) {
//         UserActivityLog failedLog = new UserActivityLog(
//             user,
//             "LOGIN",
//             ipAddress,
//             false
//         );
//         failedLog.setUserAgent(userAgent);
//         failedLog.setFailureReason(reason);
//         userActivityLogRepository.save(failedLog);
//     }
    
//     // Helper method to create user response
//     private UserResponse createUserResponse(User user) {
//         return new UserResponse(
//             user.getId(),
//             user.getUsername(),
//             user.getEmail(),
//             user.getFullName(),
//             user.getIsEmailVerified(),
//             user.getTotalAuraPoints(),
//             user.getCurrentStreak(),
//             user.getLongestStreak(),
//             user.getCreatedAt()
//         );
//     }
// }