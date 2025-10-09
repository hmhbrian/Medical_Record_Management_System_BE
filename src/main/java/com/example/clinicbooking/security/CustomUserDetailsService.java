package com.example.clinicbooking.security;

import com.example.clinicbooking.entity.User;
import com.example.clinicbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

import static com.example.clinicbooking.Utils.TextUtils.normalizeText;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String PhoneNumber) throws UsernameNotFoundException {
        String phone = normalizeText(PhoneNumber);
        User user = userRepo.findByPhoneNumber(phone)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        System.out.println("UserDetailsService password(from entity) = " + user.getPass());

//        return org.springframework.security.core.userdetails.User
//                .withUsername(user.getPhoneNumber())
//                .password(user.getPass())
//                .authorities(getAuthorities(user))   // map role -> GrantedAuthority
//                .accountLocked(false)
//                .disabled(false)
//                .build();
        return new CustomUserDetails(user);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User u) {
        String role = switch (u.getRole()) {
            case 0 -> "ROLE_ADMIN";
            case 1 -> "ROLE_PATIENT";
            case 2 -> "ROLE_STAFF";
            default -> "ROLE_USER";
        };
        return List.of(new SimpleGrantedAuthority(role));
    }

//    public String mapRole(int role) {
//        return switch (role) {
//            case 0 -> "ROLE_ADMIN";
//            case 1 -> "ROLE_PATIENT";
//            case 2 -> "ROLE_STAFF";
//            default -> "ROLE_UNKNOWN";
//        };
//    }
}
