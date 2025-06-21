package com.aurameter.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${app.base-url}")
    private String baseUrl;
    
    // Send email verification
    public void sendVerificationEmail(String email, String token) {
        String verificationUrl = baseUrl + "/api/auth/verify-email?token=" + token;
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("AuraMeter - Verify Your Email");
        message.setText(
            "Welcome to AuraMeter!\n\n" +
            "Please click the link below to verify your email address:\n" +
            verificationUrl + "\n\n" +
            "This link will expire in 24 hours.\n\n" +
            "If you didn't create an account with AuraMeter, please ignore this email.\n\n" +
            "Best regards,\n" +
            "The AuraMeter Team"
        );
        
        // For development, we'll just log the email instead of sending
        System.out.println("=== EMAIL VERIFICATION ===");
        System.out.println("To: " + email);
        System.out.println("Subject: " + message.getSubject());
        System.out.println("Message: " + message.getText());
        System.out.println("Verification URL: " + verificationUrl);
        System.out.println("========================");
        
        // Uncomment the line below when you configure real SMTP
        // mailSender.send(message);
    }
    
    // Send password reset email
    public void sendPasswordResetEmail(String email, String token) {
        String resetUrl = baseUrl + "/api/auth/reset-password?token=" + token;
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("AuraMeter - Password Reset");
        message.setText(
            "Hi there,\n\n" +
            "You requested to reset your password for your AuraMeter account.\n\n" +
            "Please click the link below to reset your password:\n" +
            resetUrl + "\n\n" +
            "This link will expire in 1 hour.\n\n" +
            "If you didn't request this password reset, please ignore this email.\n\n" +
            "Best regards,\n" +
            "The AuraMeter Team"
        );
        
        // For development, we'll just log the email instead of sending
        System.out.println("=== PASSWORD RESET EMAIL ===");
        System.out.println("To: " + email);
        System.out.println("Subject: " + message.getSubject());
        System.out.println("Message: " + message.getText());
        System.out.println("Reset URL: " + resetUrl);
        System.out.println("===========================");
        
        // Uncomment the line below when you configure real SMTP
        // mailSender.send(message);
    }
}