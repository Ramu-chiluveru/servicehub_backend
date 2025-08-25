package com.servicehub.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "requests", indexes = {
        @Index(name = "idx_request_user", columnList = "user_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Requests {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "image")
    private String image;

    // Default value is "pending", and can later be updated to "completed" or "cancelled"
    @Column(name = "status", nullable = false)
    @Builder.Default
    private String status = "pending";

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "priority", nullable = false)
    private String priority;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Proposal> proposals;

    @Column(name="acceptedProposalId")
    private Long acceptedProposalId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "completed_by")
    private User provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
