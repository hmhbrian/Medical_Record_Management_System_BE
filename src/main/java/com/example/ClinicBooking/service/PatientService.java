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
public class PatientService {
    private final UserRepository userRepo;
    private final PatientRepository patientRepo;
    private final PasswordEncoder passwordEncoder;

    public PatientService(UserRepository userRepo, PatientRepository patientRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.patientRepo = patientRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Patient createPatient(PatientRequest request) {
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
        return patientRepo.save(patient);
    }

    public List<PatientResponse> getAll() {
        List<Patient> patients = patientRepo.findAll();

        return patients.stream().map(patient -> {
            PatientResponse dto = new PatientResponse();
            dto.setPatientcode(patient.getPatientcode());
            dto.setFullname(patient.getUser().getFullname());
            dto.setEmail(patient.getUser().getEmail());
            dto.setAddress(patient.getUser().getAddress());
            dto.setPhoneNumber(patient.getUser().getPhoneNumber());
            dto.setDateOfBirth(patient.getUser().getDateOfBirth());
            dto.setGender(patient.getUser().getGender());
            dto.setMedicalHistory(patient.getMedicalHistory());
            dto.setInsuranceNumber(patient.getInsuranceNumber());
            return dto;
        }).collect(Collectors.toList());
    }
}
