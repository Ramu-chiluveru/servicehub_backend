package com.servicehub.controller;

import com.servicehub.dto.BookingRequest;
import com.servicehub.model.Bookings;
import com.servicehub.repository.BookingRepository;
import com.servicehub.repository.ServiceCategoryRepository;
import com.servicehub.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import com.servicehub.model.ServiceCategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.Authenticator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ServiceCategoryRepository serviceRepo;
    
    @Autowired
    private UserRepository userRepo;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest,Authentication authentication) {
        try {
            String email = authentication.getName();
            ServiceCategory serviceCategory = serviceRepo.findById(bookingRequest.getServiceId()).orElseThrow(() -> new RuntimeException("Service not found"));
            Long providerId = serviceCategory.getUser().getId();
            Long customerId = userRepo.findByEmail(email).getId();
            
            Bookings booking = Bookings.builder()
                .serviceId(bookingRequest.getServiceId())
                .providerId(providerId)
                .customerId(customerId)
                .build();
            
            Bookings savedBooking = bookingRepository.save(booking);
            
            return ResponseEntity.ok(savedBooking);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Error creating booking: " + e.getMessage());
        }
    }
}