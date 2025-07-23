//
//package com.servicehub.service;
//
//import com.servicehub.dto.PendingJobsDTO;
//import com.servicehub.dto.ServiceDTO;
//import com.servicehub.model.Requests;
//import com.servicehub.model.ServiceCategory;
//import com.servicehub.model.User;
//import com.servicehub.model.Locations;
//import com.servicehub.repository.ProviderRepository;
//import com.servicehub.repository.UserRepository;
//import com.servicehub.repository.LocationsRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class ProviderService {
//
//    @Autowired
//    private ProviderRepository providerRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private LocationsRepository locationsRepository;
//
//    public List<PendingJobsDTO> getNearbyPendingJobs(String email)
//    {
//        User provider = userRepository.findByEmail(email);
//
//        // Get latest location for this provider
//        Locations providerLocation = locationsRepository
//                .findTopByUserIdOrderByIdDesc(provider.getId())
//                .orElseThrow(() -> new RuntimeException("Provider location not found"));
//
//        // Get coordinates (latitude is Y, longitude is X)
//        double lat = providerLocation.getLocation().getY();
//        double lng = providerLocation.getLocation().getX();
//
//        List<Requests> nearbyRequests = providerRepository.findPendingRequestsNearProvider(lat, lng);
//
//        return nearbyRequests.stream().map(request -> PendingJobsDTO.builder()
//                .id(String.valueOf(request.getId()))
//                .customer(request.getUser().getFirstName() + " " + request.getUser().getLastName())
//                .category(request.getCategory())
//                .description(request.getDescription())
//                .price(request.getPrice())
//                .status(request.getStatus())
//                .createdAt(request.getCreatedAt())
//                .location(request.getUser().getLocation())
//                .priority(request.getPriority())
//                .build()
//        ).collect(Collectors.toList());
//    }
//
//
//    public void addService(String email,ServiceDTO request)
//    {
//        User provider = userRepository.findByEmail(email);
//        ServiceCategory serviceCategory = ServiceCategory.builder()
//                .category(request.getCategory())
//                .description(request.getDescripton())
//                .price(request.getPrice())
//                .servicename(request.getServicename())
//                .user(provider)
//                .build();
//
//        providerRepository.save(serviceCategory);
//
//    }
//}

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
    ServiceCategoryRepository serviceCategoryRepository;

    /**
     * Get all pending jobs near the provider based on their latest location
     */
    public List<PendingJobsDTO> getNearbyPendingJobs(String email) {
        User provider = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new RuntimeException("Provider not found with email: " + email));

        // Get latest location for this provider
        Locations providerLocation = locationsRepository
                .findTopByUserIdOrderByIdDesc(provider.getId())
                .orElseThrow(() -> new RuntimeException("Provider location not found"));

        // Extract coordinates (PostGIS point → latitude = Y, longitude = X)
        double lat = providerLocation.getLocation().getY();
        double lng = providerLocation.getLocation().getX();

        List<Requests> nearbyRequests = providerRepository.findPendingRequestsNearProvider(lat, lng);

        return nearbyRequests.stream().map(request -> PendingJobsDTO.builder()
                .id(String.valueOf(request.getId()))
                .customer(request.getUser().getFirstName() + " " + request.getUser().getLastName())
                .category(request.getCategory())
                .description(request.getDescription())
                .price(request.getPrice())
                .status(request.getStatus())
                .createdAt(request.getCreatedAt())
                .location(
                        request.getUser().getLocation() != null
                                ? request.getUser().getLocation()
                                : "Location not available"
                )
                .priority(request.getPriority())
                .build()
        ).collect(Collectors.toList());
    }


    @Transactional
    public void addService(String email, ServiceDTO request) {
        User provider = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new RuntimeException("Provider not found with email: " + email));

        ServiceCategory serviceCategory = ServiceCategory.builder()
                .category(request.getCategory())
                .description(request.getDescripton())
                .price(request.getPrice())
                .servicename(request.getServicename())
                .user(provider)
                .build();

        serviceCategoryRepository.save(serviceCategory);
    }

    public List<ServiceDTO> getServices(String email)
    {
        User user = userRepository.findByEmail(email);
        List<ServiceCategory> services = serviceCategoryRepository.findAllByUserId(user.getId());

        return  services.stream().map(service -> ServiceDTO.builder()
                .id(service.getUser().getId())
                .price(service.getPrice())
                .servicename(service.getServicename())
                .descripton(service.getDescription())
                .category(service.getCategory())
                .build()
        ).collect(Collectors.toList());
    }
}
