package com.example.clinicbooking.config;

import com.example.clinicbooking.security.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public final class CurrentUser {
    private CurrentUser() {}

    public static Integer id() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails u)) return null;
        return u.getId();
    }
}
