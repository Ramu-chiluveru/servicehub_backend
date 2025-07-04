package com.servicehub.security;

import com.fasterxml.jackson.annotation.ObjectIdGenerators.None;
import com.servicehub.model.User;
import com.servicehub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (!userRepo.existsByEmail(email)) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        User user = userRepo.findByEmail(email);
        String role = user.getRole();

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(role)
                .build();
    }

}
