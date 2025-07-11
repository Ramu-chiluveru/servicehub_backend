package com.servicehub.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name="role")
    private String role;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "street")
    private String street = null;

    @Column(name = "village")
    private String village = null;

    @Column(name = "mandal")
    private String mandal;

    @Column(name = "district")
    private String district;

    @Column(name = "state")
    private String state;

    @Column(name = "pincode", length = 6)
    private String pincode;

    @Column(name = "active")
    private boolean active;

    @Column(name="suspended")
    private boolean suspended;

    @Column(name = "image_url")
    private String imageUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public String getLocation()
    {
        StringBuilder sb = new StringBuilder();

        if (street != null && !street.isBlank()) sb.append(street).append(", ");
        if (village != null && !village.isBlank()) sb.append(village).append(", ");
        if (mandal != null && !mandal.isBlank()) sb.append(mandal).append(", ");
        if (district != null && !district.isBlank()) sb.append(district).append(", ");
        if (state != null && !state.isBlank()) sb.append(state).append(", ");
        if (pincode != null && !pincode.isBlank()) sb.append("Pincode: ").append(pincode);


        String location = sb.toString().trim();
        if (location.endsWith(",")) {
            location = location.substring(0, location.length() - 1);
        }

        return location;
    }


}
