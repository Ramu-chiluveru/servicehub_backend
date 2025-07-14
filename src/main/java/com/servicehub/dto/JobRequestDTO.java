package com.servicehub.dto;

import lombok.Data;

@Data
public class JobRequestDTO {
  private String category;
  private String description;
  private Double price;
  private String image;
  private String priority;
  private String status;
}
