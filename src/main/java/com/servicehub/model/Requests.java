package com.servicehub.model;
//
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.*;
//
//@Entity
//@Table(name = "requests", indexes = {
//    @Index(name = "idx_request_status", columnList = "status"),
//    @Index(name = "idx_request_user", columnList = "user_id")
//})
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Requests {
//
//    public enum RequestStatus {
//        PENDING, COMPLETED,CONFIRMED, REJECTED
//    }
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "category", nullable = false)
//    private String category;
//
//    @Column(name = "description", columnDefinition = "TEXT")
//    private String description;
//
//    @Column(name = "price")
//    private Double price;
//
//    @Column(name = "image")
//    private String image;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "status")
//    @Builder.Default
//    private RequestStatus status = RequestStatus.PENDING;
//
//    @CreationTimestamp
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//
//    @Column(name = "completed_at")
//    private LocalDateTime completedAt;
//
//    @UpdateTimestamp
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//    // @Column(name = "final_price")
//    // private Double finalPrice;
//
//    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Proposal> proposals;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//}

@Entity
@Table(name = "requests", indexes = {
        @Index(name = "idx_request_status", columnList = "status"),
        @Index(name = "idx_request_user", columnList = "user_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Requests {

    public enum RequestStatus {
        PENDING, COMPLETED, CONFIRMED, REJECTED
    }

    @Id
    @Column(name = "id", insertable = false)
    private String id;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "image")
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private RequestStatus status = RequestStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name="priority")
    private  String priority;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Proposal> proposals;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "completed_by")
    private  User provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
