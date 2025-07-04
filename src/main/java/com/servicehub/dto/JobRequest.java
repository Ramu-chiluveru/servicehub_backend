package com.servicehub.dto;

import lombok.Data;

@Data
public class JobRequest {
  private String category;
  private String description;
  private Double price;
  private String image;
}
