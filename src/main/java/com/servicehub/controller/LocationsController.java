package com.servicehub.controller;

import com.servicehub.dto.LocationRequestDTO;
import com.servicehub.model.User;
import com.servicehub.service.LocationService;
import com.servicehub.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/user")
public class LocationsController {

    private final LocationService locationService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(LocationsController.class);

    public LocationsController(LocationService locationService, UserService userService) {
        this.locationService = locationService;
        this.userService = userService;
    }

    @PostMapping("/location")
    public ResponseEntity<String> saveLocation(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody LocationRequestDTO request) {

        if (userDetails == null) {
            return ResponseEntity.status(401).body("Unauthorized: No user details found");
        }

        String email = userDetails.getUsername();
        logger.info("Location request received from user: {}", email);

        User user = userService.getProfile(email);
        if (user == null) {
            return ResponseEntity.status(404).body("User profile not found");
        }

        locationService.saveUserLocation(user.getId(), request.getLatitude(), request.getLongitude());
        return ResponseEntity.ok("Location saved successfully");
    }
}
