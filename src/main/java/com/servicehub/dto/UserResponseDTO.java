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
  }
}
