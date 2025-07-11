package com.servicehub.service;

import com.servicehub.dto.PendingJobsDTO;
import com.servicehub.model.Requests;
import com.servicehub.repository.ProviderRepository;
import com.servicehub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProviderService {

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private UserRepository userRepository;

    public List<PendingJobsDTO> getAllPendingJobs() {
        List<Requests> pendingRequests = providerRepository.findAllPendingRequestsOrderByCreatedAtDesc();

        return pendingRequests.stream().map(request -> PendingJobsDTO.builder()
                .id(String.valueOf(request.getId()))
                .customer(request.getUser().getFirstName() + " " + request.getUser().getLastName())
                .category(request.getCategory())
                .description(request.getDescription())
                .price(request.getPrice())
                .status(request.getStatus())
                .createdAt(request.getCreatedAt())
                .location(request.getUser().getLocation()) // calling getLocation() method in User
                .priority(request.getPriority())
                .build()
        ).collect(Collectors.toList());
    }
}
