package com.aurameter.backend.exception;

import com.aurameter.backend.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    // Handle user not found
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException ex, 
            HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getMessage(),
            "USER_NOT_FOUND",
            HttpStatus.NOT_FOUND.value(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    
    // Handle invalid credentials
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(
            InvalidCredentialsException ex, 
            HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            "Invalid email/username or password",  // Generic message for security
            "INVALID_CREDENTIALS",
            HttpStatus.UNAUTHORIZED.value(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
    
    // Handle account locked
    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<ErrorResponse> handleAccountLocked(
            AccountLockedException ex, 
            HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            "Account is temporarily locked due to multiple failed login attempts. Please try again later or reset your password.",
            "ACCOUNT_LOCKED",
            HttpStatus.FORBIDDEN.value(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
    
    // Handle email already exists
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(
            EmailAlreadyExistsException ex, 
            HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getMessage(),
            "EMAIL_ALREADY_EXISTS",
            HttpStatus.CONFLICT.value(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
    
    // Handle invalid tokens
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidToken(
            InvalidTokenException ex, 
            HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getMessage(),
            "INVALID_TOKEN",
            HttpStatus.UNAUTHORIZED.value(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
    
    // Handle validation errors (from @Valid annotations)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, 
            HttpServletRequest request) {
        
        List<String> validationErrors = new ArrayList<>();
        
        // Extract all field validation errors
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            validationErrors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
            "Validation failed",
            "VALIDATION_ERROR",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI(),
            validationErrors
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    // Handle all other unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, 
            HttpServletRequest request) {
        
        // Log the full error for debugging (don't expose to user)
        System.err.println("Unexpected error: " + ex.getMessage());
        ex.printStackTrace();
        
        ErrorResponse errorResponse = new ErrorResponse(
            "An unexpected error occurred. Please try again later.",
            "INTERNAL_SERVER_ERROR",
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}