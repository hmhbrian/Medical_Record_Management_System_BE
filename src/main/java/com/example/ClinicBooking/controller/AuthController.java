package com.example.ClinicBooking.controller;

import com.example.ClinicBooking.DTO.LoginRequest;
import com.example.ClinicBooking.DTO.LoginResponse;
import com.example.ClinicBooking.DTO.PatientRequest;
import com.example.ClinicBooking.DTO.PatientResponse;
import com.example.ClinicBooking.config.JwtService;
import com.example.ClinicBooking.entity.Patient;
import com.example.ClinicBooking.entity.Staff;
import com.example.ClinicBooking.entity.User;
import com.example.ClinicBooking.repository.StaffRepository;
import com.example.ClinicBooking.repository.UserRepository;
import com.example.ClinicBooking.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private PatientService patientService;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getPhoneNumber(), request.getPassword())
            );
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        User user = userRepo.findByPhoneNumber(request.getPhoneNumber()).get();
        String position = null;
        String roleString;
        switch (user.getRole()) {
            case 0:
                roleString = "ADMIN";
                break;
            case 1:
                roleString = "PATIENT";
                break;
            case 2:
                roleString = "STAFF";
                Staff staff = staffRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new RuntimeException("Staff profile not found"));
                position = staff.getPosition().getPosition();
                break;
            default:
                throw new RuntimeException("Invalid role");
        }

        String token = jwtService.generateToken(user, position);

        LoginResponse response = new LoginResponse();
        response.setUserId(user.getId());
        response.setToken(token);
        response.setRole(roleString);
        response.setPosition(position);
        response.setFullname(user.getFullname());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<PatientResponse> create(@RequestBody PatientRequest request) {
        return ResponseEntity.ok(patientService.create(request));
    }

}

