package com.servicehub.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProposalDTO 
{
    private Long id;
    private String message;
    private Double proposedPrice;
    private String providerName;
    private Double providerRating;
    private LocalDateTime submittedAt;
    private Long providerId;
}
