package com.servicehub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PendingJobsDTO
{
    private String id;
    private String customer;
    private String category;
    private String description;
    private Double price;
    private String status;
    private LocalDateTime createdAt;
    private String location;
    private String priority;
}
