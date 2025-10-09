package com.example.clinicbooking.security;

import com.example.clinicbooking.entity.Staff;
import com.example.clinicbooking.entity.User;
import com.example.clinicbooking.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static com.example.clinicbooking.service.StaffPositionService.getPositionByUserId;

public class CustomUserDetails implements UserDetails {
    private Integer id;
    private String username;
    private String password;
    private String position;
    private String fullname;
    private boolean accountNonLocked;
    private boolean enabled;
    private final List<GrantedAuthority> authorities;

    public CustomUserDetails(User u) {
        this.id = u.getId();
        this.username = u.getPhoneNumber();
        this.fullname = u.getFullname();
        this.password = u.getPass(); // hoặc getPassword() nếu field tên là password
        this.position = u.getRole() == 2 ? getPositionByUserId(u.getId()) : "Unknown";
        String role = switch (u.getRole()) {
            case 0 -> "ROLE_ADMIN";
            case 1 -> "ROLE_PATIENT";
            case 2 -> "ROLE_STAFF";
            default -> "ROLE_USER";
        };
        this.authorities = List.of(new SimpleGrantedAuthority(role));
    }

    public Integer getId() {
        return id;
    }
    public String getPosition() {
        return position;
    }
    public String getFullname() {
        return fullname;
    }
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Assuming account never expires
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Assuming credentials never expire
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
