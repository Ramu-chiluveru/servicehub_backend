package com.servicehub.controller;

import com.servicehub.dto.UpdateRequest;
import com.servicehub.dto.UserResponse;
import com.servicehub.model.User;
import com.servicehub.security.JwtUtil;
import com.servicehub.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired private UserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User request) {
        logger.info("Registration request received for email: {}", request.getEmail());

        if (userService.existsByEmail(request.getEmail())) {
            logger.warn("Attempt to register with already existing email: {}", request.getEmail());
            return ResponseEntity.badRequest().body("Email already exists");
        }

    // Create user
        userService.createUser(request);
        logger.info("User registered successfully with email: {}", request.getEmail());

        // Build response

        return ResponseEntity.ok(Map.of(
            "message", "User registered successfully"
        ));
    }


    @PostMapping("/role")
    public ResponseEntity<?> setRole(@RequestBody User request) {
        logger.info("Role set request received for email: {}", request.getEmail());

        if (!userService.existsByEmail(request.getEmail())) {
            logger.warn("User with this email not existed: {}", request.getEmail());
            return ResponseEntity.badRequest().body("User with this email not existed");
        }

        userService.updateUserRole(request);
        return ResponseEntity.ok("Role set successfully");
    }

    @PostMapping("/address")
    public ResponseEntity<?> setAddress(@RequestBody User request) {
        logger.info("Address set request received for email: {}", request.getEmail());

        if (!userService.existsByEmail(request.getEmail())) {
            logger.warn("User with this email not existed: {}", request.getEmail());
            return ResponseEntity.badRequest().body("User with this email not existed");
        }

        var userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        User user = userService.updateUserAddress(request);
        return ResponseEntity.ok(Map.of("role",user.getRole(),"token",token));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) 
    {
        String email = authentication.getName(); 
        logger.info("Profile request for: {}", email);

        if (!userService.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userService.getProfile(email);
        System.out.println("Phone registration: "+user.getPhone());
        UserResponse userResponse = new UserResponse(user);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(Authentication authentication, @RequestBody UpdateRequest request) {
        
        String email = authentication.getName();
        logger.info("Update request for: {}", email);

        if (!userService.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("User with this email not existed");
        }

        User updatedUser = userService.updateProfileDetails(email, request);
        return ResponseEntity.ok(updatedUser);
    }
}
