package com.servicehub.service;

import com.servicehub.dto.EditRequestDTO;
import com.servicehub.dto.JobRequestDTO;
import com.servicehub.dto.ProposalDTO;
import com.servicehub.dto.RequestDTO;
import com.servicehub.model.Requests;
import com.servicehub.model.User;
import com.servicehub.repository.CustomerRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RequestMapper requestMapper;

    public void addJob(User user, JobRequestDTO jobRequestDTO) {
        LocalDate today = LocalDate.now();
        String dateStr = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int count = customerRepository.countByCreatedAtDate(today);

        String customId = "REQ_" + dateStr + "_" + (count + 1);

        Requests request = Requests.builder()
                .id(customId)
                .category(jobRequestDTO.getCategory())
                .description(jobRequestDTO.getDescription())
                .price(jobRequestDTO.getPrice())
                .priority(jobRequestDTO.getPriority())
                .user(user)
                .build();

        customerRepository.save(request);
    }

    @Transactional(readOnly = true)
    public List<RequestDTO> getJobs(Long userId) {
        List<Requests> jobs = customerRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return jobs.stream()
                .map(requestMapper::toRequestDTO)
                .collect(Collectors.toList());
    }

    public List<ProposalDTO> getProposals(String requestId) {
        return customerRepository.findWithProposalsById(requestId);
    }

    public void updateJob(String id, JobRequestDTO jobRequestDTO) {
        Requests request = customerRepository.findById(id);
        if (request == null) {
            throw new IllegalArgumentException("Request not found with id: " + id);
        }

        if (jobRequestDTO.getCategory() != null && !Objects.equals(request.getCategory(), jobRequestDTO.getCategory())) {
            request.setCategory(jobRequestDTO.getCategory());
        }

        if (jobRequestDTO.getStatus() != null && !Objects.equals(request.getStatus(), jobRequestDTO.getStatus())) {
            request.setStatus(jobRequestDTO.getStatus());
        }

        if (jobRequestDTO.getPriority() != null && !Objects.equals(request.getPriority(), jobRequestDTO.getPriority())) {
            request.setPriority(jobRequestDTO.getPriority());
        }

        if (jobRequestDTO.getDescription() != null && !Objects.equals(request.getDescription(), jobRequestDTO.getDescription())) {
            request.setDescription(jobRequestDTO.getDescription());
        }

        if (jobRequestDTO.getPrice() != null && !Objects.equals(request.getPrice(), jobRequestDTO.getPrice())) {
            request.setPrice(jobRequestDTO.getPrice());
        }

        request.setCreatedAt(LocalDateTime.now());

        customerRepository.save(request);
    }

    public EditRequestDTO getJobDetails(String id) {
        Requests request = customerRepository.findById(id);
        if (request == null) {
            throw new IllegalArgumentException("Request not found with id: " + id);
        }

        return EditRequestDTO.builder()
                .category(request.getCategory())
                .price(request.getPrice())
                .priority(request.getPriority())
                .description(request.getDescription())
                .build();
    }
}
