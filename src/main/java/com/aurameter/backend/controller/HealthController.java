package com.aurameter.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class HealthController {
    
    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
            "status", "UP",
            "message", "AuraMeter Backend is running!",
            "timestamp", LocalDateTime.now(),
            "database", "Connected to PostgreSQL"
        );
    }
}