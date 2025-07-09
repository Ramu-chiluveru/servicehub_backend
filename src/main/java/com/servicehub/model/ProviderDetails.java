package com.servicehub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "provider_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderDetails
{
    @Id
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "name")
    private  String name;
}
