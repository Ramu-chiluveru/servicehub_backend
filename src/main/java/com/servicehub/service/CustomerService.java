package com.servicehub.service;

import com.servicehub.dto.JobRequest;
import com.servicehub.dto.ProposalDTO;
import com.servicehub.dto.RequestDTO;
import com.servicehub.model.Requests;
import com.servicehub.model.User;
import com.servicehub.repository.CustomerRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService 
{

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RequestMapper requestMapper;

    public void addJob(User user, JobRequest jobRequest) {
        LocalDate today = LocalDate.now();
        String dateStr = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int count = customerRepository.countByCreatedAtDate(today);

        String customId = "REQ_" + dateStr + "_" + (count + 1);

        Requests request = Requests.builder()
                .id(customId)
                .category(jobRequest.getCategory())
                .description(jobRequest.getDescription())
                .price(jobRequest.getPrice())
                .status(Requests.RequestStatus.PENDING)
                .priority(Requests.getPr)
                .user(user)
                .build();

        customerRepository.save(request);
    }

    @Transactional(readOnly = true)
    public List<RequestDTO> getJobs(Long userId)
    {
        List<Requests> jobs = customerRepository.findByUserId(userId);
        return jobs.stream()
                .map(requestMapper::toRequestDTO)
                .collect(Collectors.toList());
    }

    public List<ProposalDTO> getProposals(String requestId)
    {
        return customerRepository.findWithProposalsById(requestId);
    }
}