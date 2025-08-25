package com.servicehub.repository;

import com.servicehub.model.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Bookings, Long> {
    List<Bookings> findByCustomerId(Long customerId);
    List<Bookings> findByProviderId(Long providerId);
    List<Bookings> findByServiceId(Long serviceId);
}