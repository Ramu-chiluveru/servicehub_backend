package com.servicehub.dto;

import lombok.Data;

@Data
public class LocationRequest {
    private Long userId;
    private double latitude;
    private double longitude;
}
