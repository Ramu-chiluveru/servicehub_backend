package com.servicehub.service;

import com.servicehub.dto.UpdateRequest;
import com.servicehub.model.User;
import com.servicehub.repository.UserRepository;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    public boolean existsByEmail(String email) 
    {
        return userRepo.existsByEmail(email);
    }

    public User createUser(@RequestBody User request) 
    {
        User user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        return userRepo.save(user);
    }

    public User updateUserRole(@RequestBody User request)
    {
        User user = userRepo.findByEmail(request.getEmail());
        user.setRole(request.getRole());
        return userRepo.save(user);
    }

    public User updateUserAddress(@RequestBody User request)
    {
        
        User user = userRepo.findByEmail(request.getEmail());
        System.out.println("Phone registration: "+request.getPhone());
        user.setPhone(request.getPhone());
        user.setStreet(request.getStreet());
        user.setVillage(request.getVillage());
        user.setMandal(request.getMandal());
        user.setDistrict(request.getDistrict());
        user.setPincode(request.getPincode());
        return userRepo.save(user);
    }

    public User getProfile(String email)
    { 
      return userRepo.findByEmail(email);
    }


    public User updateProfileDetails(String email, UpdateRequest request) 
    {
        User updUser = userRepo.findByEmail(email);
        if (updUser == null) throw new RuntimeException("User not found");

        if (request.getFirstName() != null)
            updUser.setFirstName(request.getFirstName());

        if (request.getLastName() != null)
            updUser.setLastName(request.getLastName());

        if (request.getPhone() != null)
            updUser.setPhone(request.getPhone());

        if (request.getCurrentPassword() != null && request.getNewPassword() != null) 
        {
            boolean match = passwordEncoder.matches(request.getCurrentPassword(), updUser.getPassword());
            if (!match) {
                throw new IllegalArgumentException("Current password is incorrect");
            }
            updUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        return userRepo.save(updUser);
    }

}

