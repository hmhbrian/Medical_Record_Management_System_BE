package com.example.ClinicBooking.service.impl;

import com.example.ClinicBooking.entity.User;
import com.example.ClinicBooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userRepo.findByPhoneNumber(phone)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getPhoneNumber(),
                user.getPass(),
                Collections.singletonList(new SimpleGrantedAuthority(mapRole(user.getRole())))
        );
    }

    public String mapRole(int role) {
        return switch (role) {
            case 0 -> "ROLE_ADMIN";
            case 1 -> "ROLE_PATIENT";
            case 2 -> "ROLE_STAFF";
            default -> "ROLE_UNKNOWN";
        };
    }
}
