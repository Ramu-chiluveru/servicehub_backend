package com.servicehub.controller;

import com.servicehub.dto.UserResponse;
import com.servicehub.model.User;
import com.servicehub.repository.UserRepository;
import com.servicehub.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")  
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired private AuthenticationManager authManager;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        logger.info("Received login request: {}", request); 

        if (request == null || !request.containsKey("email") || !request.containsKey("password")) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email and password are required"));
        }

        String email = request.get("email");
        String password = request.get("password");

        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String token = jwtUtil.generateToken(userDetails);
        User user = userRepository.findByEmail(email);
        UserResponse userResponse = new UserResponse(user);

        return ResponseEntity.ok(Map.of(
            "token", token,
            "role",user.getRole()
        ));
    }
}
