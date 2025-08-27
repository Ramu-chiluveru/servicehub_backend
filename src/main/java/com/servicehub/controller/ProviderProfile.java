package com.servicehub.controller;

import com.servicehub.dto.UserResponseDTO;
import com.servicehub.model.User;
import com.servicehub.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/provider")
public class ProviderProfile {
    private static final Logger logger = LoggerFactory.getLogger(ProviderProfile.class);

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication)
    {
        String email = authentication.getName();
        logger.info("Provider profile request for: {}", email);

        if (!userService.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userService.getProfile(email);

        // Check if the user is actually a provider
        if (!"PROVIDER".equals(user.getRole())){
            return ResponseEntity.badRequest().body("User is not a provider");
        }

        UserResponseDTO userResponseDTO = new UserResponseDTO(user);
        return ResponseEntity.ok(userResponseDTO);
    }
}