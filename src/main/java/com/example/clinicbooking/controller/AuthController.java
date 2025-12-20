package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.Auth.ChangePassDTO;
import com.example.clinicbooking.DTO.Auth.LoginRequest;
import com.example.clinicbooking.DTO.Auth.LoginResponse;
import com.example.clinicbooking.DTO.Patient.PatientRequest;
import com.example.clinicbooking.DTO.Patient.PatientResponse;
import com.example.clinicbooking.config.CurrentUser;
import com.example.clinicbooking.config.JwtService;
import com.example.clinicbooking.entity.Staff;
import com.example.clinicbooking.entity.User;
import com.example.clinicbooking.repository.StaffRepository;
import com.example.clinicbooking.repository.UserRepository;
import com.example.clinicbooking.security.CustomUserDetails;
import com.example.clinicbooking.service.PatientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.clinicbooking.Utils.TextUtils.normalizeText;

@Tag(name = "Auth", description = "Xác thực và quản lý người dùng")
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
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        String phone = normalizeText(request.getPhoneNumber());
        String raw = normalizeText(request.getPassword());

        User user = userRepo.findByPhoneNumber(phone).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Số điện thoại không tồn tại");
        }

        if (!passwordEncoder.matches(raw, user.getPass())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai số điện thoại hoặc mật khẩu");
        }

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
                position = staff.getStaff_position().getPosition();
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



    @GetMapping("/me")
    public ResponseEntity<?> getMe() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }
        Integer currentUserId = cud.getId();

        // principal có thể là "anonymousUser" (String) nếu chưa đăng nhập
        return ResponseEntity.ok(userRepo.findById(currentUserId));
    }
}
