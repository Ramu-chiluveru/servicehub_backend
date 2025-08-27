package com.servicehub.dto;

import java.time.LocalDateTime;
import com.servicehub.model.User;
import lombok.Data;

@Data
public class UserResponseDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String street;
    private String village;
    private String role;
    private String mandal;
    private String district;
    private String pincode;
    private String state;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean suspended;
    private boolean active;

    // Provider-specific fields (will be null for non-providers)
    private String profession;
    private String category;
    private String experience;
    private String skills;
    private String description;
    private Double rating;
    private Integer reviewsCount;
    private String serviceLocation;

    // Computed fields
    private String fullName;
    private String location;
    private String address;

    public UserResponseDTO(User user) {
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
        this.street = user.getStreet();
        this.village = user.getVillage();
        this.mandal = user.getMandal();
        this.district = user.getDistrict();
        this.pincode = user.getPincode();
        this.state = user.getState();
        this.imageUrl = user.getImageUrl();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.suspended = user.isSuspended();
        this.active = user.isActive();
        this.role = user.getRole();

        // Computed fields
        this.fullName = user.getFirstName() + " " + user.getLastName();
        this.location = user.getLocation();
        this.address = user.getLocation(); // For backward compatibility

        // Only set provider fields if user is a provider
        if ("PROVIDER".equals(user.getRole())) {
            this.profession = user.getProfession();
            this.category = user.getCategory();
            this.experience = user.getExperience();
            this.skills = user.getSkills();
            this.description = user.getDescription();
            this.rating = user.getRating();
            this.reviewsCount = user.getReviewsCount();
            this.serviceLocation = user.getServiceLocation();
        }
    }
}