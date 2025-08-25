package com.servicehub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bookings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "service_id")
    private Long serviceId;
    
    @Column(name = "provider_id")
    private Long providerId;
    
    @Column(name = "customer_id")
    private Long customerId;
    
    @Column(name = "booking_date")
    private LocalDateTime bookingDate;
    
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum BookingStatus {
        PENDING, CONFIRMED, COMPLETED, CANCELLED
    }
    
    @PrePersist
    protected void onCreate() {
        updatedAt = LocalDateTime.now();
        status = BookingStatus.PENDING;
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}