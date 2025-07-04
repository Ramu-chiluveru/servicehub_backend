package com.servicehub.controller;

import com.servicehub.dto.JobRequest;
import com.servicehub.model.Requests;
import com.servicehub.model.User;
import com.servicehub.security.JwtUtil;
import com.servicehub.service.CustomerService;
import com.servicehub.service.UserService;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.servicehub.dto.ProposalDTO;
import com.servicehub.dto.RequestDTO;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired private CustomerService customerService;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @PostMapping("/job")
    public ResponseEntity<?> submitJob(Authentication authentication,@RequestBody JobRequest jobRequest) {
        String email = authentication.getName();
        logger.info("Job request received from user: {}", email);

        User user = userService.getProfile(email);  
        customerService.addJob(user, jobRequest);  

        return ResponseEntity.ok("Job request submitted successfully");
    }

    @GetMapping("/jobs")
    public ResponseEntity<?> jobs(Authentication authentication)
    {
        String email = authentication.getName();
        logger.info("Jobs request received from user: {}",email);
        User user = userService.getProfile(email);

        List<RequestDTO> requests = customerService.getJobs(user.getId());
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/proposals")
    public ResponseEntity<?> proposals(Authentication authentication,@Param("request_id") Long requestId)
    {
        String email = authentication.getName();
        logger.info("Proposals request received from user: {}",email);
        List<ProposalDTO> proposalDTOs = customerService.getProposals(requestId);
        return ResponseEntity.ok(proposalDTOs);
    }
}
