package com.servicehub.controller;

import com.servicehub.dto.PendingJobsDTO;
import com.servicehub.dto.RequestDTO;
import com.servicehub.dto.ServiceDTO;
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

    @GetMapping("/raisedjobs")
    public ResponseEntity<?> getMethodName(Authentication authentication) 
    {
        String email = authentication.getName();
        logger.info("Raised jobs requested by provider: {}", email);
        try {
            List<RequestDTO> jobs = providerService.getRaisedJobs(email);
            logger.info("Found {} jobs near provider", jobs.size());
            return ResponseEntity.ok(jobs);
        } catch (RuntimeException e) {
            logger.error("Error fetching nearby jobs: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    

    @PostMapping("/service")
    public ResponseEntity<?> addService(Authentication authentication,@RequestBody ServiceDTO request)
    {
        String email = authentication.getName();
        logger.info("addService requested by provider: {}", email);

        try {
            providerService.addService(email,request);
            return ResponseEntity.ok("Service added successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/service/{serviceId}")
    public ResponseEntity<?> updateService(Authentication authentication,@RequestBody ServiceDTO request,@PathVariable Long serviceId)
    {
        String email = authentication.getName();
        logger.info("addService requested by provider: {}", email);
        try {
            providerService.updateService(serviceId,request);
            return ResponseEntity.ok("Service updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/services")
    public ResponseEntity<List<ServiceDTO>> services(Authentication authentication)
    {
        String email = authentication.getName();
        logger.info("Services request recieved from provider: {}",email);

        try {
            List<ServiceDTO> serviceDTOS = providerService.getServices(email);
            logger.info("Found {} jobs near provider", serviceDTOS.size());
            return ResponseEntity.ok(serviceDTOS);
        } catch (RuntimeException e) {
            logger.error("Error fetching nearby jobs: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/service/{id}")
    public ResponseEntity<?> deleteService(@PathVariable("id") Long serviceId, Authentication authentication) {
        String email = authentication.getName(); // Get the provider's email from authentication
        try {
            providerService.deleteService(email, serviceId); // Call service layer
            return ResponseEntity.ok("Service deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("raiseproposal/{id}")
    public ResponseEntity<?> raiseProposal(@PathVariable String id,Authentication authentication) 
    {
        String email = authentication.getName();

        try{
            logger.info("raisepropsal request recieved from provider: {} for requestid: {}",email,id);
            providerService.raiseProposal(id,email);
        }
        catch(RuntimeException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok("success");
        
    }

}