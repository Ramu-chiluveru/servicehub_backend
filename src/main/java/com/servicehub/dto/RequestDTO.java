package com.servicehub.dto;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestDTO {
    private Long id;
    private String category;
    private String description;
    private Double price;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}