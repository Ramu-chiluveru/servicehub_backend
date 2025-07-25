package com.servicehub.service;

import com.servicehub.dto.PendingJobsDTO;
import com.servicehub.dto.ServiceDTO;
import com.servicehub.model.Requests;
import com.servicehub.model.ServiceCategory;
import com.servicehub.model.User;
import com.servicehub.model.Locations;
import com.servicehub.repository.ProviderRepository;
import com.servicehub.repository.ServiceCategoryRepository;
import com.servicehub.repository.UserRepository;
import com.servicehub.repository.LocationsRepository;

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
    private LocationsRepository locationsRepository;

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;

    /**
     * Get all pending jobs near the provider based on their latest location
     */
    public List<PendingJobsDTO> getNearbyPendingJobs(String email) {
        User provider = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new RuntimeException("Provider not found with email: " + email));

        List<Requests> nearbyRequests;

        try {
//            Locations providerLocation = locationsRepository
//                    .findTopByUserIdOrderByIdDesc(provider.getId())
//                    .orElseThrow(() -> new RuntimeException("Provider location not found"));
//
//            double lat = providerLocation.getLocation().getY(); // Latitude
//            double lng = providerLocation.getLocation().getX(); // Longitude

//            nearbyRequests = providerRepository.findPendingRequestsNearProvider(lat, lng);
            nearbyRequests = providerRepository.findPendingRequestsNearProvider();
        } catch (Exception e) {
            // fallback in case location is unavailable
            nearbyRequests = providerRepository.findPendingRequestsNearProvider();
        }

        return nearbyRequests.stream().map(request -> PendingJobsDTO.builder()
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
                .build()
        ).collect(Collectors.toList());
    }

    /**
     * Provider adds a new service offering
     */
    @Transactional
    public void addService(String email, ServiceDTO request) {
        User provider = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new RuntimeException("Provider not found with email: " + email));

        ServiceCategory serviceCategory = ServiceCategory.builder()
                .category(request.getCategory())
                .description(request.getDescription())
                .price(request.getPrice())
                .servicename(request.getServicename())
                .user(provider)
                .build();

        serviceCategoryRepository.save(serviceCategory);
    }

    /**
     * Get all services offered by the provider
     */
    public List<ServiceDTO> getServices(String email) {
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new RuntimeException("Provider not found with email: " + email));

        List<ServiceCategory> services = serviceCategoryRepository.findAllByUserId(user.getId());

        return services.stream().map(service -> ServiceDTO.builder()
                .id(service.getId())
                .price(service.getPrice())
                .servicename(service.getServicename())
                .description(service.getDescription())
                .category(service.getCategory())
                .build()
        ).collect(Collectors.toList());
    }

    /**
     * Update an existing service offered by the provider
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
     * Delete a service offered by the provider
     */
    @Transactional
    public void deleteService(String email, Long serviceId) {
        ServiceCategory service = serviceCategoryRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + serviceId));

        serviceCategoryRepository.delete(service);
    }
}
