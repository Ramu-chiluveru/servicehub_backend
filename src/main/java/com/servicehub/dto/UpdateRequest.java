package com.servicehub.dto;

import lombok.Data;

@Data
public class UpdateRequest 
{
  private String firstName;
  private String lastName;
  private String phone;
  private String currentPassword;
  private String newPassword;
}
