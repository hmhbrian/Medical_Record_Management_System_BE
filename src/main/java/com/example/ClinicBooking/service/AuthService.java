package com.example.ClinicBooking.service;

import com.example.ClinicBooking.DTO.LoginRequest;
import com.example.ClinicBooking.DTO.LoginResponse;
import com.example.ClinicBooking.config.JwtUtil;
import com.example.ClinicBooking.entity.User;
import com.example.ClinicBooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        User user = userRepo.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy số điện thoại"));

        if (!user.getPass().equals(request.getPassword())) {
            throw new BadCredentialsException("Sai mật khẩu");
        }

        String token = jwtUtil.generateToken(user);

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setRole(user.getRole());
        response.setFullname(user.getFullname());

        return response;
    }
}

