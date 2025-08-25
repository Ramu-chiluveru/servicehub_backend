// package com.servicehub.service;

// import com.servicehub.dto.PendingJobsDTO;
// import com.servicehub.dto.ServiceDTO;
// import com.servicehub.dto.RequestDTO;
// import com.servicehub.model.Requests;
// import com.servicehub.model.ServiceCategory;
// import com.servicehub.model.User;
// import com.servicehub.model.Proposal;
// import com.servicehub.repository.ProviderRepository;
// import com.servicehub.repository.ServiceCategoryRepository;
// import com.servicehub.repository.UserRepository;
// import com.servicehub.repository.LocationsRepository;
// import com.servicehub.repository.ProposalRepo;
// import com.servicehub.repository.RequestsRepo;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.util.List;
// import java.util.Objects;
// import java.util.Optional;
// import java.util.stream.Collectors;

// @Service
// public class ProviderService {

//     @Autowired
//     private ProviderRepository providerRepository;

//     @Autowired
//     private UserRepository userRepository;

//     @Autowired
//     private LocationsRepository locationsRepository;

//     @Autowired
//     private ServiceCategoryRepository serviceCategoryRepository;

//     @Autowired
//     private ProposalRepo proposalRepo;
    
//     @Autowired
//     private RequestsRepo requestsRepo;

//     @Autowired
//     private RequestMapper requestMapper; // FIXED: Typo was @Autwired

//     /**
//      * Get all pending jobs near the provider based on their latest location
//      */
//     public List<PendingJobsDTO> getNearbyPendingJobs(String email) {
//         User provider = Optional.ofNullable(userRepository.findByEmail(email))
//                 .orElseThrow(() -> new RuntimeException("Provider not found with email: " + email));

//         Long providerId = provider.getId();

//         List<Requests> nearbyRequests;
//         try {
//             nearbyRequests = requestsRepo.findPendingRequestsNearProvider(providerId);
//         } catch (Exception e) {
//             nearbyRequests = requestsRepo.findPendingRequestsNearProvider(providerId);
//         }

//         return nearbyRequests.stream().map(request -> PendingJobsDTO.builder()
//                 .id(String.valueOf(request.getId()))
//                 .customer(request.getUser().getFirstName() + " " + request.getUser().getLastName())
//                 .category(request.getCategory())
//                 .description(request.getDescription())
//                 .price(request.getPrice())
//                 .status(request.getStatus())
//                 .createdAt(request.getCreatedAt())
//                 .location(request.getUser().getLocation() != null
//                         ? request.getUser().getLocation()
//                         : "Location not available")
//                 .priority(request.getPriority())
//                 .build()
//         ).collect(Collectors.toList());
//     }

//     public List<RequestDTO> getRaisedJobs(String email) {
//         User provider = Optional.ofNullable(userRepository.findByEmail(email))
//                 .orElseThrow(() -> new RuntimeException("Provider not found with email: " + email));

//         Long providerId = provider.getId();
//         List<Requests> jobs = requestsRepo.findproposalRaisedJobs(providerId);

//         return jobs.stream()
//                 .map(requestMapper::toRequestDTO)
//                 .collect(Collectors.toList());
//     }

//     @Transactional
//     public void addService(String email, ServiceDTO request) {
//         User provider = Optional.ofNullable(userRepository.findByEmail(email))
//                 .orElseThrow(() -> new RuntimeException("Provider not found with email: " + email));

//         ServiceCategory serviceCategory = ServiceCategory.builder()
//                 .category(request.getCategory())
//                 .description(request.getDescription())
//                 .price(request.getPrice())
//                 .servicename(request.getServicename())
//                 .user(provider)
//                 .ratings(0.0)
//                 .reviews(0)
//                 .build();

//         serviceCategoryRepository.save(serviceCategory);
//     }

//     public List<ServiceDTO> getServices()
//     {
//         List<ServiceCategory> services = serviceCategoryRepository.findAll();
//         return services.stream().map(service -> ServiceDTO.builder()
//             .id(service.getId())
//             .price(service.getPrice())
//             .servicename(service.getServicename())
//             .description(service.getDescription())
//             .category(service.getCategory())
//             .ratings(0.0)
//             .reviews(0)
//             .build()
//         ).collect(Collectors.toList());
//     }

//     public List<ServiceDTO> getServices(String email) {
//         User user = Optional.ofNullable(userRepository.findByEmail(email))
//                 .orElseThrow(() -> new RuntimeException("Provider not found with email: " + email));

//         List<ServiceCategory> services = serviceCategoryRepository.findAllByUserId(user.getId());

//         return services.stream().map(service -> ServiceDTO.builder()
//                 .id(service.getId())
//                 .price(service.getPrice())
//                 .servicename(service.getServicename())
//                 .description(service.getDescription())
//                 .category(service.getCategory())
//                 .ratings(0.0)
//                 .reviews(0)
//                 .build()
//         ).collect(Collectors.toList());
//     }

//     @Transactional
//     public void updateService(Long serviceId, ServiceDTO request) {
//         ServiceCategory service = serviceCategoryRepository.findById(serviceId)
//                 .orElseThrow(() -> new RuntimeException("Service not found with id: " + serviceId));

//         if (request.getCategory() != null && !Objects.equals(service.getCategory(), request.getCategory())) {
//             service.setCategory(request.getCategory());
//         }

//         if (request.getServicename() != null && !Objects.equals(service.getServicename(), request.getServicename())) {
//             service.setServicename(request.getServicename());
//         }

//         if (request.getDescription() != null && !Objects.equals(service.getDescription(), request.getDescription())) {
//             service.setDescription(request.getDescription());
//         }

//         if (request.getPrice() != 0 && !Objects.equals(service.getPrice(), request.getPrice())) {
//             service.setPrice(request.getPrice());
//         }

//         serviceCategoryRepository.save(service);
//     }

//     @Transactional
//     public void deleteService(String email, Long serviceId) {
//         ServiceCategory service = serviceCategoryRepository.findById(serviceId)
//                 .orElseThrow(() -> new RuntimeException("Service not found with id: " + serviceId));

//         serviceCategoryRepository.delete(service);
//     }

//     public void raiseProposal(String requestId, String providerEmail) {
//         User providerUser = userRepository.findByEmail(providerEmail);

//         Requests requests = requestsRepo.findById(requestId)
//                 .orElseThrow(() -> new IllegalArgumentException("Request not found with id: " + requestId));

//         Proposal newProposal = Proposal.builder()
//                 .provider(providerUser)
//                 .providerRating(3)
//                 .request(requests)
//                 .build();

//         proposalRepo.save(newProposal);
//     }

//     public void deleteProposal(String requestId,String email)
//     {
//         User provider = userRepository.findByEmail(email);
//         Long providerId = provider.getId();
//         Long proposalId = proposalRepo.getReferenceById(requestId,providerId);
//         proposalRepo.deleteById(proposalId);   
//     }
// }


package com.servicehub.service;

import com.servicehub.dto.PendingJobsDTO;
import com.servicehub.dto.ServiceDTO;
import com.servicehub.dto.RequestDTO;
import com.servicehub.model.Requests;
import com.servicehub.model.ServiceCategory;
import com.servicehub.model.User;
import com.servicehub.model.Proposal;
import com.servicehub.repository.ProviderRepository;
import com.servicehub.repository.ServiceCategoryRepository;
import com.servicehub.repository.UserRepository;
import com.servicehub.repository.ProposalRepo;
import com.servicehub.repository.RequestsRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProviderService {

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;

    @Autowired
    private ProposalRepo proposalRepo;

    @Autowired
    private RequestsRepo requestsRepo;

    @Autowired
    private RequestMapper requestMapper;

    /**
     * Get all pending jobs near the provider.
     */
    public List<PendingJobsDTO> getNearbyPendingJobs(String email) {
        User provider = userRepository.findByEmail(email);
        if (provider == null) {
            throw new RuntimeException("Provider not found with email: " + email);
        }

        Long providerId = provider.getId();
        List<Requests> nearbyRequests = requestsRepo.findPendingRequestsNearProvider(providerId);

        return nearbyRequests.stream()
                .map(request -> PendingJobsDTO.builder()
                        .id(String.valueOf(request.getId()))
                        .customer(request.getUser().getFirstName() + " " + request.getUser().getLastName())
                        .category(request.getCategory())
                        .description(request.getDescription())
                        .price(request.getPrice())
                        .status(request.getStatus())
                        .createdAt(request.getCreatedAt())
                        .location(request.getUser().getLocation() != null
                                ? request.getUser().getLocation()
                                : "Location not available")
                        .priority(request.getPriority())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Get raised jobs where the provider has submitted proposals.
     */
    public List<RequestDTO> getRaisedJobs(String email) {
        User provider = userRepository.findByEmail(email);
        if (provider == null) {
            throw new RuntimeException("Provider not found with email: " + email);
        }

        Long providerId = provider.getId();
        List<Requests> jobs = requestsRepo.findproposalRaisedJobs(providerId);

        return jobs.stream()
                .map(requestMapper::toRequestDTO)
                .collect(Collectors.toList());
    }

    /**
     * Add a new service for the provider.
     */
    @Transactional
    public void addService(String email, ServiceDTO request) {
        User provider = userRepository.findByEmail(email);
        if (provider == null) {
            throw new RuntimeException("Provider not found with email: " + email);
        }

        ServiceCategory serviceCategory = ServiceCategory.builder()
                .category(request.getCategory())
                .description(request.getDescription())
                .price(request.getPrice())
                .servicename(request.getServicename())
                .user(provider)
                .ratings(0.0)
                .reviews(0)
                .build();

        serviceCategoryRepository.save(serviceCategory);
    }

    /**
     * Get all services (for customers).
     */
    public List<ServiceDTO> getServices() {
        return serviceCategoryRepository.findAll().stream()
                .map(service -> ServiceDTO.builder()
                        .id(service.getId())
                        .price(service.getPrice())
                        .servicename(service.getServicename())
                        .description(service.getDescription())
                        .category(service.getCategory())
                        .ratings(0.0)
                        .reviews(0)
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Get all services for a specific provider.
     */
    public List<ServiceDTO> getServices(String email) {
        User provider = userRepository.findByEmail(email);
        if (provider == null) {
            throw new RuntimeException("Provider not found with email: " + email);
        }

        return serviceCategoryRepository.findAllByUserId(provider.getId()).stream()
                .map(service -> ServiceDTO.builder()
                        .id(service.getId())
                        .price(service.getPrice())
                        .servicename(service.getServicename())
                        .description(service.getDescription())
                        .category(service.getCategory())
                        .ratings(0.0)
                        .reviews( 0)
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Update an existing service.
     */
    @Transactional
    public void updateService(Long serviceId, ServiceDTO request) {
        ServiceCategory service = serviceCategoryRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + serviceId));

        if (request.getCategory() != null && !Objects.equals(service.getCategory(), request.getCategory())) {
            service.setCategory(request.getCategory());
        }
        if (request.getServicename() != null && !Objects.equals(service.getServicename(), request.getServicename())) {
            service.setServicename(request.getServicename());
        }
        if (request.getDescription() != null && !Objects.equals(service.getDescription(), request.getDescription())) {
            service.setDescription(request.getDescription());
        }
        if (request.getPrice() != 0 && !Objects.equals(service.getPrice(), request.getPrice())) {
            service.setPrice(request.getPrice());
        }

        serviceCategoryRepository.save(service);
    }

    /**
     * Delete a service by ID.
     */
    @Transactional
    public void deleteService(String email, Long serviceId) {
        ServiceCategory service = serviceCategoryRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + serviceId));

        serviceCategoryRepository.delete(service);
    }

    /**
     * Raise a proposal for a specific request.
     */
    public void raiseProposal(String requestId, String providerEmail) {
        User providerUser = userRepository.findByEmail(providerEmail);
        if (providerUser == null) {
            throw new RuntimeException("Provider not found with email: " + providerEmail);
        }

        Requests requests = requestsRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found with id: " + requestId));

        Proposal newProposal = Proposal.builder()
                .provider(providerUser)
                .providerRating(3)
                .request(requests)
                .build();

        proposalRepo.save(newProposal);
    }

    /**
     * Delete a proposal by request ID and provider email.
     */
    public void deleteProposal(String requestId, String email) {
        User provider = userRepository.findByEmail(email);
        if (provider == null) {
            throw new RuntimeException("Provider not found with email: " + email);
        }
        Long providerId = provider.getId();
        Long proposalId = proposalRepo.getReferenceById(requestId, providerId);
        proposalRepo.deleteById(proposalId);
    }
}
