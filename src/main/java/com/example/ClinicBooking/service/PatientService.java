package com.example.ClinicBooking.service;

import com.example.ClinicBooking.DTO.DoctorResponse;
import com.example.ClinicBooking.DTO.PatientRequest;
import com.example.ClinicBooking.DTO.PatientResponse;
import com.example.ClinicBooking.entity.Doctor;
import com.example.ClinicBooking.entity.Patient;
import com.example.ClinicBooking.entity.User;
import com.example.ClinicBooking.repository.PatientRepository;
import com.example.ClinicBooking.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService implements IUserService<PatientResponse,PatientRequest> {
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
            throw new RuntimeException("Email đã được sử dụng");
        }

        User user = new User();
        user.setFullname(request.fullname);
        user.setEmail(request.email);
        user.setAddress(request.address);
        user.setPhoneNumber(request.phoneNumber);
        user.setDateOfBirth(request.dateOfBirth);
        user.setGender(request.gender);
        user.setRole(1);
        user.setPass(passwordEncoder.encode(request.password));
        userRepo.save(user);

        Patient patient = new Patient();
        patient.setUser(user);
        patient.setMedicalHistory(request.medicalHistory);
        patient.setInsuranceNumber(request.insuranceNumber);

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
            throw new IllegalArgumentException("User ID cannot be null");
        }

        try {
            // Use Optional directly instead of stream
            Patient patient = patientRepo.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + userId));
            return covertToResponse(patient);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lấy bệnh nhân", e);
        }
    }

    @Transactional
    public PatientResponse update(Integer id, PatientRequest request) {
        Patient patient = patientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân"));

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
        patientRepo.save(patient);

        return covertToResponse(patient);
    }

    @Transactional
    public void delete(Integer id) {
        Patient patient = patientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân"));

        User user = patient.getUser();
        patientRepo.delete(patient);
        userRepo.delete(user);
    }

    private PatientResponse covertToResponse(Patient patient) {
        PatientResponse dto = new PatientResponse();
        dto.setId(patient.getId());
        dto.setPatientcode(patient.getPatientcode());
        dto.setFullname(patient.getUser().getFullname());
        dto.setEmail(patient.getUser().getEmail());
        dto.setAddress(patient.getUser().getAddress());
        dto.setPhoneNumber(patient.getUser().getPhoneNumber());
        dto.setDateOfBirth(patient.getUser().getDateOfBirth());
        dto.setGender(patient.getUser().getGender());
        dto.setAvatar_url(patient.getUser().getAvatar_url());
        dto.setMedicalHistory(patient.getMedicalHistory());
        dto.setInsuranceNumber(patient.getInsuranceNumber());
        return dto;
    }
}
