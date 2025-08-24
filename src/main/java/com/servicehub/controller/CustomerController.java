package com.servicehub.controller;

import com.servicehub.dto.AcceptProposalDTO;
import com.servicehub.dto.EditRequestDTO;
import com.servicehub.dto.JobRequestDTO;
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
import com.servicehub.dto.AcceptProposalDTO;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired private CustomerService customerService;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @PostMapping("/job")
    public ResponseEntity<?> submitJob(Authentication authentication,@RequestBody JobRequestDTO jobRequestDTO) {
        String email = authentication.getName();
        logger.info("Job request received from user: {}", email);

        User user = userService.getProfile(email);  
        customerService.addJob(user, jobRequestDTO);

        return ResponseEntity.ok("Job request submitted successfully");
    }


    @PutMapping("/job/{id}")
    public ResponseEntity<?> updateJob(Authentication authentication, @RequestBody JobRequestDTO jobRequestDTO, @PathVariable String id) {
        String email = authentication.getName();
        logger.info("Job update request received from user: {}", email);

        customerService.updateJob(id, jobRequestDTO);

        return ResponseEntity.ok("Job update request submitted successfully");
    }

    @GetMapping("/job/{id}")
    public ResponseEntity<?> getJob(Authentication authentication,@PathVariable String id) {
        String email = authentication.getName();
        logger.info("Job details request received from user: {}", email);

         EditRequestDTO editRequestDTO = customerService.getJobDetails(id);
        return ResponseEntity.ok(editRequestDTO);
    }

    @PutMapping("/proposal/")
    public ResponseEntity<?> acceptProposal(Authentication authentication,@RequestBody AcceptProposalDTO proposalPayload) 
     {
        try{
            String email = authentication.getName();
            logger.info("Accept proposal request received from user: "+email + " for proposal: "+proposalPayload.getProposalId());
            customerService.updateProposal(proposalPayload.getRequestId(),proposalPayload.getProposalId());
        }  
        catch(Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("success");
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<RequestDTO>> jobs(Authentication authentication)
    {
        String email = authentication.getName();
        logger.info("Jobs request received from user: {}", email);
        User user = userService.getProfile(email);
        List<RequestDTO> requests = customerService.getJobs(user.getId());
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/proposals")
    public ResponseEntity<?> proposals(Authentication authentication,@Param("request_id") String requestId)
    {
        String email = authentication.getName();
        logger.info("Proposals request received from user: {}",email);
        List<ProposalDTO> proposalDTOs = customerService.getProposals(requestId);
        return ResponseEntity.ok(proposalDTOs);
    }
}
