package com.servicehub.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceDTO
{
    private String servicename;
    private String descripton;
    private double price;
    private String category;
    private Long id;
}
