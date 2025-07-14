//package com.servicehub.controller;
//
//import com.servicehub.dto.PendingJobsDTO;
//import com.servicehub.dto.RequestDTO;
//import com.servicehub.model.User;
//import com.servicehub.service.ProviderService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
//@RequestMapping("/api/provider")
//public class ProviderController {
//
//    @Autowired
//    private ProviderService providerService;
//    private static final Logger logger = LoggerFactory.getLogger(ProviderController.class);
//
//    @GetMapping("/jobs")
//    public ResponseEntity<List<PendingJobsDTO>> jobs(Authentication authentication) {
//        String email = authentication.getName();
//        logger.info("Nearby jobs requested by provider: {}", email);
//        List<PendingJobsDTO> jobs = providerService.getNearbyPendingJobs(email);
//        return ResponseEntity.ok(jobs);
//    }
//
//}

package com.servicehub.controller;

import com.servicehub.dto.PendingJobsDTO;
import com.servicehub.service.ProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/provider")
public class ProviderController {

    private final ProviderService providerService;
    private static final Logger logger = LoggerFactory.getLogger(ProviderController.class);

    @Autowired
    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<PendingJobsDTO>> jobs(Authentication authentication) {
        String email = authentication.getName();
        logger.info("Nearby jobs requested by provider: {}", email);
        try {
            List<PendingJobsDTO> jobs = providerService.getNearbyPendingJobs(email);
            logger.info("Found {} jobs near provider", jobs.size());
            return ResponseEntity.ok(jobs);
        } catch (RuntimeException e) {
            logger.error("Error fetching nearby jobs: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}