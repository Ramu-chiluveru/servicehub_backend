package com.servicehub.service;

import com.servicehub.dto.JobRequest;
import com.servicehub.dto.ProposalDTO;
import com.servicehub.dto.RequestDTO;
import com.servicehub.model.Requests;
import com.servicehub.model.User;
import com.servicehub.repository.CustomerRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService 
{

    @Autowired
    private CustomerRepository customerRepository;

    public Requests addJob(User user, JobRequest jobRequest) 
    {
        Requests request = Requests.builder()
            .category(jobRequest.getCategory())
            .description(jobRequest.getDescription())
            .price(jobRequest.getPrice())
            .image(jobRequest.getImage())
            .user(user)
            .build();

        return customerRepository.save(request);
    }

    @Transactional(readOnly = true)
    public List<RequestDTO> getJobs(Long userId) 
    {
        List<Requests> jobs = customerRepository.findByUserId(userId);
        List<RequestDTO> requestDTOs = new ArrayList<>();

        for (Requests job : jobs) {
            RequestDTO request = new RequestDTO(
                job.getId(),
                job.getCategory(),
                job.getDescription(),
                job.getPrice(),
                job.getStatus().toString(),
                job.getCreatedAt(),
                job.getCompletedAt()
            );
            requestDTOs.add(request);
        }

        return requestDTOs;
    }

    public List<ProposalDTO> getProposals(Long requestId) 
    {
        return customerRepository.findWithProposalsById(requestId);
    }
}