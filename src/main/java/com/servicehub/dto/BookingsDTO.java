package com.servicehub.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingsDTO 
{
  private Long id;
  private Long providerId;
  private Long serviceId;
  private String status; 
  private String category;
  private String title;
  private String description;
  private double price;
  private LocalDateTime createdAt;
  private LocalDateTime completedAt;
  private String providerName;
}
