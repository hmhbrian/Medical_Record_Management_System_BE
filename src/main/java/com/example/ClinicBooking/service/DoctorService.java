package com.example.ClinicBooking.service;

import com.example.ClinicBooking.DTO.DoctorRequest;
import com.example.ClinicBooking.DTO.DoctorResponse;
import com.example.ClinicBooking.entity.Doctor;
import com.example.ClinicBooking.entity.Staff;
import com.example.ClinicBooking.entity.User;
import com.example.ClinicBooking.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService implements IDoctorService {
    private final DoctorRepository doctorRepo;
    private final SpecialtyRepository specialtyRepo;
    private StaffCreationService staffCreationService;

    public DoctorService(DoctorRepository doctorRepo,
                         SpecialtyRepository specialtyRepo,
                         StaffCreationService staffCreationService) {
        this.doctorRepo = doctorRepo;
        this.specialtyRepo = specialtyRepo;
        this.staffCreationService = staffCreationService;
    }

    @Override
    @Transactional
    public DoctorResponse create(DoctorRequest request){
        User user = staffCreationService.createUser(request);
        Staff staff = staffCreationService.createStaff(user, request.getDepartmentId(), request.getPositionId());

        Doctor doctor = new Doctor();
        doctor.setStaff(staff);
        doctor.setSpecialty(specialtyRepo.findById(request.specialtyId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên khoa")));
        doctor.setExperienceYears(request.experienceYears);
        doctor.setCertificationName(request.certificationName);
        doctor.setIssuedBy(request.issuedBy);
        doctor.setIssueDate(request.issueDate);

        doctorRepo.save(doctor);
        return covertToResponse(doctor);
    }

    @Override
    public List<DoctorResponse> getAll() {
        return doctorRepo.findAll().stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    private DoctorResponse covertToResponse(Doctor doctor) {
        DoctorResponse dto = new DoctorResponse();
        dto.setDoctorcode(doctor.getDoctorcode());
        dto.setFullname(doctor.getStaff().getUser().getFullname());
        dto.setEmail(doctor.getStaff().getUser().getEmail());
        dto.setAddress(doctor.getStaff().getUser().getAddress());
        dto.setPhoneNumber(doctor.getStaff().getUser().getPhoneNumber());
        dto.setDateOfBirth(doctor.getStaff().getUser().getDateOfBirth());
        dto.setGender(doctor.getStaff().getUser().getGender());
        dto.setDepartment(doctor.getStaff().getDepartment().getName());
        dto.setSpecialty(doctor.getSpecialty().getName());
        dto.setExperienceYears(doctor.getExperienceYears());
        dto.setCertificationName(doctor.getCertificationName());
        dto.setIssuedBy(doctor.getIssuedBy());
        dto.setIssueDate(doctor.getIssueDate());
        return dto;
    }

    @Override
    public List<DoctorResponse> getDoctorsBySpecialtyId(Integer specialtyId) {
        try {
            return doctorRepo.findBySpecialtyId(specialtyId).stream()
                    .map(this::covertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace(); // hoặc dùng logger.error(...)
            throw new RuntimeException("Lỗi khi lấy danh sách bác sĩ theo chuyên khoa", e);
        }
    }

}
