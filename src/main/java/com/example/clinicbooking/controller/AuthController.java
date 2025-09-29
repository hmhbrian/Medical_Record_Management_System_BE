package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.Auth.ChangePassDTO;
import com.example.clinicbooking.DTO.Auth.LoginRequest;
import com.example.clinicbooking.DTO.Auth.LoginResponse;
import com.example.clinicbooking.DTO.Patient.PatientRequest;
import com.example.clinicbooking.DTO.Patient.PatientResponse;
import com.example.clinicbooking.config.JwtService;
import com.example.clinicbooking.entity.Staff;
import com.example.clinicbooking.entity.User;
import com.example.clinicbooking.repository.StaffRepository;
import com.example.clinicbooking.repository.UserRepository;
import com.example.clinicbooking.security.CustomUserDetails;
import com.example.clinicbooking.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static com.example.clinicbooking.Utils.TextUtils.normalizeText;

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
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
//        try {
//            String phoneNumber = normalizeText(request.getPhoneNumber());
//            String password = normalizeText(request.getPassword());
//            if (phoneNumber.isEmpty() || password.isEmpty()) {
//                return ResponseEntity.badRequest().body("Phone number and password must not be empty");
//            }
//            authManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(phoneNumber, password)
//            );
//        } catch (AuthenticationException ex) {
//            System.out.println("phone=" + request.getPhoneNumber()+ " pass=" + request.getPassword());
//            System.out.println(passwordEncoder.encode(request.getPassword()));
//            System.out.println("Authentication failed: " + ex.getMessage());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid PhoneNumber or password");
//        }

        String phone = normalizeText(request.getPhoneNumber());
        String raw   = normalizeText(request.getPassword());

        User user = userRepo.findByPhoneNumber(phone)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai thông tin"));

        if (!passwordEncoder.matches(raw, user.getPass())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid PhoneNumber or password");
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

    @PutMapping("/Changepass/{id}")
    public ResponseEntity<?> ChangePass(@PathVariable int id, @RequestBody ChangePassDTO newPass) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân"));

        String raw = normalizeText(newPass.getNewPassword());
        String encoded = passwordEncoder.encode(raw);
        user.setPass(encoded);
        userRepo.save(user);

        //test:
        boolean ok = passwordEncoder.matches(raw, user.getPass());
        System.out.println("matches after save? " + ok + " | len=" + user.getPass().length());

        return ResponseEntity.ok("Change pass successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        if (principal instanceof CustomUserDetails cud) {
            return ResponseEntity.ok(Map.of(
                    "id", cud.getId(),
                    "fullname", cud.getFullname(),
                    "phone", cud.getUsername(),
                    "position", cud.getPosition(),
                    "authorities", cud.getAuthorities()
            ));
        }

        // principal có thể là "anonymousUser" (String) nếu chưa đăng nhập
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

}
