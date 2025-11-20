package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.Patient.PatientRequest;
import com.example.clinicbooking.DTO.Patient.PatientResponse;
import com.example.clinicbooking.entity.Patient;
import com.example.clinicbooking.entity.User;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService implements IPatientService {
    private final UserRepository userRepo;
    private final PatientRepository patientRepo;
    private final PasswordEncoder passwordEncoder;

    public PatientService(UserRepository userRepo, PatientRepository patientRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.patientRepo = patientRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public PatientResponse create(PatientRequest request) {
        if(userRepo.findByEmail(request.email).isPresent()) {
            throw new InvalidInputException("Email đã được sử dụng");
        }

        User user = new User();
        user.setFullname(request.fullname);
        user.setEmail(request.email);
        user.setAddress(request.address);
        user.setPhoneNumber(request.phoneNumber);
        user.setDateOfBirth(request.dateOfBirth);
        user.setGender(request.gender);
        user.setAvatar_url(request.avatar_url);
        user.setRole(1);
        user.setPass(passwordEncoder.encode(request.password));
        userRepo.save(user);

        Patient patient = new Patient();
        patient.setUser(user);
        patient.setMedicalHistory(request.medicalHistory);
        patient.setInsuranceNumber(request.insuranceNumber);
        patient.setInsuranceRate(request.insuranceRate);

        patientRepo.save(patient);
        return covertToResponse(patient);
    }

    @Override
    public List<PatientResponse> getAll() {
        return patientRepo.findAll().stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PatientResponse getbyUserId(Integer userId) {
        if (userId == null) {
            throw new InvalidInputException("User ID cannot be null");
        }

        try {
            // Use Optional directly instead of stream
            Patient patient = patientRepo.findByUserId(userId)
                    .orElseThrow(() -> new InvalidInputException("Patient not found!"));
            return covertToResponse(patient);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lấy bệnh nhân", e);
        }
    }

    @Transactional
    public PatientResponse update(Integer id, PatientRequest request) {
        Patient patient = patientRepo.findById(id)
                .orElseThrow(() -> new InvalidInputException("Không tìm thấy bệnh nhân"));

        User user = patient.getUser();

        // Update user information
        user.setFullname(request.fullname);
        user.setEmail(request.email);
        user.setPhoneNumber(request.phoneNumber);
        user.setDateOfBirth(request.dateOfBirth);
        user.setGender(request.gender);
        user.setAddress(request.address);
        user.setAvatar_url(request.avatar_url);

        // Update password if provided
        if (request.password != null && !request.password.isEmpty()) {
            user.setPass(passwordEncoder.encode(request.password));
        }

        userRepo.save(user);

        // Update patient information
        patient.setMedicalHistory(request.medicalHistory);
        patient.setInsuranceNumber(request.insuranceNumber);
        patient.setInsuranceRate(request.insuranceRate);
        patientRepo.save(patient);

        return covertToResponse(patient);
    }

    @Transactional
    public void delete(Integer id) {
        Patient patient = patientRepo.findById(id)
                .orElseThrow(() -> new InvalidInputException("Không tìm thấy bệnh nhân"));

        User user = patient.getUser();
        patientRepo.delete(patient);
        userRepo.delete(user);
    }

    public PatientResponse searchPatients(String keyword) {
        // Chuẩn bị từ khóa cho điều kiện LIKE (nếu không sử dụng JpaRepository query method)
        // Tuy nhiên, JpaRepository query method đã xử lý toán tử LIKE cho chúng ta.
        // Chỉ cần truyền cùng một từ khóa cho cả 3 tham số.

        // Vì phương thức JpaRepository đã định nghĩa rõ ràng
        // findByPatientCodeContainingIgnoreCaseOrUser_PhoneNumberContainingIgnoreCaseOrUser_FullnameContainingIgnoreCase
        // nên ta chỉ cần truyền cùng 1 keyword cho cả 3 tham số.
        Optional<Patient> patient = patientRepo.findByPatientCodeContainingIgnoreCaseOrUser_PhoneNumberContainingIgnoreCaseOrUser_FullnameContainingIgnoreCase(
                keyword,
                keyword,
                keyword
        );
        return covertToResponse(patient.get());
    }

    private PatientResponse covertToResponse(Patient patient) {
        PatientResponse dto = new PatientResponse();
        dto.setId(patient.getId());
        dto.setPatientCode(patient.getPatientCode());
        dto.setFullname(patient.getUser().getFullname());
        dto.setEmail(patient.getUser().getEmail());
        dto.setAddress(patient.getUser().getAddress());
        dto.setPhoneNumber(patient.getUser().getPhoneNumber());
        dto.setDateOfBirth(patient.getUser().getDateOfBirth());
        dto.setGender(patient.getUser().getGender());
        dto.setAvatar_url(patient.getUser().getAvatar_url());
        dto.setMedicalHistory(patient.getMedicalHistory());
        dto.setInsuranceNumber(patient.getInsuranceNumber());
        dto.setInsuranceRate(patient.getInsuranceRate());
        return dto;
    }
}
