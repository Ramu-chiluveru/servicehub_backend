package com.servicehub.service;

import com.servicehub.dto.BookingsDTO;
import com.servicehub.dto.EditRequestDTO;
import com.servicehub.dto.JobRequestDTO;
import com.servicehub.dto.ProposalDTO;
import com.servicehub.dto.RequestDTO;
import com.servicehub.model.Bookings;
import com.servicehub.model.Proposal;
import com.servicehub.model.Requests;
import com.servicehub.model.ServiceCategory;
import com.servicehub.model.User;
import com.servicehub.repository.BookingRepository;
import com.servicehub.repository.CustomerRepository;
import com.servicehub.repository.ProposalRepo;
import com.servicehub.repository.RequestsRepo;
import com.servicehub.repository.ServiceCategoryRepository;
import com.servicehub.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final RequestsRepo requestsRepo;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestMapper requestMapper;

    @Autowired
    private ProposalRepo proposalRepo;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;

    CustomerService(RequestsRepo requestsRepo) {
        this.requestsRepo = requestsRepo;
    }

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

    public void updateProposal(String requestId,Long proposalId)
    {

            Requests request = requestsRepo.getReferenceById(requestId);
            Proposal proposal = proposalRepo.getReferenceById(proposalId);
            User provider = proposal.getProvider();
            request.setProvider(provider);
            request.setStatus("confirmed");

            requestsRepo.save(request);
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

    public List<BookingsDTO> getBookings(String email) 
    {
        Long customerId = userRepository.findByEmail(email).getId();
        List<Bookings> bookings = bookingRepository.findByCustomerId(customerId);  
        
        List<BookingsDTO> responsePayload = new ArrayList<>();

        for(Bookings booking: bookings)
        {
            User provider = userRepository.getReferenceById(booking.getProviderId());
            ServiceCategory service = serviceCategoryRepository.getReferenceById(booking.getServiceId());
            BookingsDTO bookingsDTO = BookingsDTO.builder()
                                        .id(booking.getId())
                                        .category(service.getCategory())
                                        .price(service.getPrice())
                                        .description(service.getDescription())
                                        .providerId(service.getUser().getId())
                                        .serviceId(service.getId())
                                        .status(booking.getStatus().name().toLowerCase()) // Corrected
                                        .title(service.getServicename())
                                        .providerName(provider.getFirstName() + " " + provider.getLastName())
                                        .createdAt(booking.getBookingDate())
                                        .completedAt(booking.getUpdatedAt())
                                    .build();
            responsePayload.add(bookingsDTO);
        }

        return responsePayload;
    }
}
