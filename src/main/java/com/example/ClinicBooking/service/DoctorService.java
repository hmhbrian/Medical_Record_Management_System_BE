package com.example.ClinicBooking.service;

import com.example.ClinicBooking.DTO.DoctorRequest;
import com.example.ClinicBooking.DTO.DoctorResponse;
import com.example.ClinicBooking.entity.Doctor;
import com.example.ClinicBooking.entity.Patient;
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

    @Override
    @Transactional
    public DoctorResponse getbyUserId(Integer userId){
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        try {
            // Use Optional directly instead of stream
            Doctor doctor = doctorRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + userId));
            return covertToResponse(doctor);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lấy bác sĩ", e);
        }
    }

    private DoctorResponse covertToResponse(Doctor doctor) {
        DoctorResponse dto = new DoctorResponse();
        dto.setId(doctor.getId());
        dto.setDoctorcode(doctor.getDoctorcode());
        dto.setFullname(doctor.getStaff().getUser().getFullname());
        dto.setEmail(doctor.getStaff().getUser().getEmail());
        dto.setAddress(doctor.getStaff().getUser().getAddress());
        dto.setPhoneNumber(doctor.getStaff().getUser().getPhoneNumber());
        dto.setDateOfBirth(doctor.getStaff().getUser().getDateOfBirth());
        dto.setAvatar_url(doctor.getStaff().getUser().getAvatar_url());
        dto.setGender(doctor.getStaff().getUser().getGender());
        dto.setDepartmentId(doctor.getStaff().getDepartment().getId());
        dto.setDepartment(doctor.getStaff().getDepartment().getName());
        dto.setPositionId(doctor.getStaff().getPosition().getId());
        dto.setSpecialtyId(doctor.getSpecialty().getId());
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
    @Override
    public DoctorResponse getDoctorsById(Integer doctorId) {
        if (doctorId == null) {
            throw new IllegalArgumentException("Doctor ID cannot be null");
        }

        try {
            // Use Optional directly instead of stream
            Doctor doctor = doctorRepo.findById(doctorId)
                    .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + doctorId));
            return covertToResponse(doctor);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lấy bác sĩ", e);
        }
    }
    @Override
    @Transactional
    public DoctorResponse update(Integer id, DoctorRequest request) {
        Doctor doctor = doctorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        User user = doctor.getStaff().getUser();
        Staff staff = doctor.getStaff();

        //cập nhật user & staff
        staffCreationService.updateUserAndStaff(user, staff, request, request.getDepartmentId(), request.getPositionId());

        // Cập nhật doctor
        doctor.setSpecialty(specialtyRepo.findById(request.specialtyId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên khoa")));
        doctor.setExperienceYears(request.experienceYears);
        doctor.setCertificationName(request.certificationName);
        doctor.setIssuedBy(request.issuedBy);
        doctor.setIssueDate(request.issueDate);
        doctorRepo.save(doctor);
        return covertToResponse(doctor);
    }

    @Transactional
    public void delete(Integer id) {
        Doctor doctor = doctorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Staff staff = doctor.getStaff();

        // Xóa doctor
        doctorRepo.delete(doctor);

        // xóa user và staff
        staffCreationService.deleteUserAndStaff(staff);
    }
}
