package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.Doctor.DoctorRequest;
import com.example.clinicbooking.DTO.Doctor.DoctorResponse;
import com.example.clinicbooking.entity.Doctor;
import com.example.clinicbooking.entity.Staff;
import com.example.clinicbooking.entity.User;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.*;
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
                .orElseThrow(() -> new InvalidInputException("Không tìm thấy chuyên khoa")));
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
            throw new InvalidInputException("User ID cannot be null");
        }

        try {
            // Use Optional directly instead of stream
            Doctor doctor = doctorRepo.findById(userId)
                    .orElseThrow(() -> new InvalidInputException("Doctor not found with ID: " + userId));
            return covertToResponse(doctor);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidInputException("Lỗi khi lấy bác sĩ");
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
        dto.setPositionId(doctor.getStaff().getStaff_position().getId());
        dto.setPosition(doctor.getStaff().getStaff_position().getPosition());
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
            throw new InvalidInputException("Lỗi khi lấy danh sách bác sĩ theo chuyên khoa");
        }
    }
    @Override
    public DoctorResponse getDoctorsById(Integer doctorId) {
        if (doctorId == null) {
            throw new InvalidInputException("Doctor ID cannot be null");
        }

        try {
            // Use Optional directly instead of stream
            Doctor doctor = doctorRepo.findById(doctorId)
                    .orElseThrow(() -> new InvalidInputException("Doctor not found with ID: " + doctorId));
            return covertToResponse(doctor);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidInputException("Lỗi khi lấy bác sĩ");
        }
    }
    @Override
    @Transactional
    public DoctorResponse update(Integer id, DoctorRequest request) {
        Doctor doctor = doctorRepo.findById(id)
                .orElseThrow(() -> new InvalidInputException("Doctor not found"));

        User user = doctor.getStaff().getUser();
        Staff staff = doctor.getStaff();

        //cập nhật user & staff
        staffCreationService.updateUserAndStaff(user, staff, request, request.getDepartmentId(), request.getPositionId());

        // Cập nhật doctor
        doctor.setSpecialty(specialtyRepo.findById(request.specialtyId)
                .orElseThrow(() -> new InvalidInputException("Không tìm thấy chuyên khoa")));
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
                .orElseThrow(() -> new InvalidInputException("Doctor not found"));

        Staff staff = doctor.getStaff();

        // Xóa doctor
        doctorRepo.delete(doctor);

        // xóa user và staff
        staffCreationService.deleteUserAndStaff(staff);
    }
}
