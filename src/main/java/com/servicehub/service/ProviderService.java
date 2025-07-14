//package com.servicehub.service;
//
//import com.servicehub.dto.PendingJobsDTO;
//import com.servicehub.model.Requests;
//import com.servicehub.repository.ProviderRepository;
//import com.servicehub.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
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
//    public List<PendingJobsDTO> getAllPendingJobs() {
//        List<Requests> pendingRequests = providerRepository.findAllPendingRequestsOrderByCreatedAtDesc();
//
//        return pendingRequests.stream().map(request -> PendingJobsDTO.builder()
//                .id(String.valueOf(request.getId()))
//                .customer(request.getUser().getFirstName() + " " + request.getUser().getLastName())
//                .category(request.getCategory())
//                .description(request.getDescription())
//                .price(request.getPrice())
//                .status(request.getStatus())
//                .createdAt(request.getCreatedAt())
//                .location(request.getUser().getLocation()) // calling getLocation() method in User
//                .priority(request.getPriority())
//                .build()
//        ).collect(Collectors.toList());
//    }
//}


package com.servicehub.service;

import com.servicehub.dto.PendingJobsDTO;
import com.servicehub.model.Requests;
import com.servicehub.model.User;
import com.servicehub.model.Locations;
import com.servicehub.repository.ProviderRepository;
import com.servicehub.repository.UserRepository;
import com.servicehub.repository.LocationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<PendingJobsDTO> getNearbyPendingJobs(String email) {
        User provider = userRepository.findByEmail(email);

        // Get latest location for this provider
        Locations providerLocation = locationsRepository
                .findTopByUserIdOrderByIdDesc(provider.getId())
                .orElseThrow(() -> new RuntimeException("Provider location not found"));

        // Get coordinates (latitude is Y, longitude is X)
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
                .location(request.getUser().getLocation())
                .priority(request.getPriority())
                .build()
        ).collect(Collectors.toList());
    }
}