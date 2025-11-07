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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
        List<Doctor> doctors = doctorRepo.findAllWithDetails();
        if (doctors.isEmpty()) {
            return Collections.emptyList(); // Trả về danh sách rỗng thay vì null
        }
        return doctors.stream()
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
        if (doctor == null) {
            return null;
        }

        DoctorResponse dto = new DoctorResponse();

        // Sử dụng Optional để bọc đối tượng Staff và tránh NPE
        Optional<Staff> staffOptional = Optional.ofNullable(doctor.getStaff());

        // Sử dụng Optional để bọc đối tượng User
        Optional<User> userOptional = staffOptional.map(Staff::getUser);

        // Truy cập các trường của User một cách an toàn
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            dto.setFullname(user.getFullname());
            dto.setEmail(user.getEmail());
            dto.setAddress(user.getAddress());
            dto.setPhoneNumber(user.getPhoneNumber());
            dto.setDateOfBirth(user.getDateOfBirth());
            dto.setAvatar_url(user.getAvatar_url());
            dto.setGender(user.getGender());
        } else {
            // Thiết lập giá trị mặc định nếu User không tồn tại (Quan trọng để tránh lỗi 500)
            dto.setFullname("Tên không xác định");
            dto.setEmail(null);
            dto.setAddress(null);
            dto.setPhoneNumber(null);
            dto.setDateOfBirth(null);
            dto.setAvatar_url(null);
            dto.setGender(1);
        }

        //Xử lý an toàn cho StaffPosition ---
        staffOptional.map(Staff::getStaff_position)
                .ifPresent(position -> {
                    dto.setPositionId(position.getId());
                    dto.setPosition(position.getPosition());
                });

        //Ánh xạ các trường của Doctor---
        dto.setId(doctor.getId());
        dto.setDoctorcode(doctor.getDoctorcode());
        dto.setExperienceYears(doctor.getExperienceYears());
        dto.setCertificationName(doctor.getCertificationName());
        dto.setIssuedBy(doctor.getIssuedBy());
        dto.setIssueDate(doctor.getIssueDate());

        //Xử lý an toàn cho Specialty ---
        if(doctor.getSpecialty() != null) {
            dto.setSpecialtyId(doctor.getSpecialty().getId());
            dto.setSpecialty(doctor.getSpecialty().getName());
        } else {
            dto.setSpecialtyId(null);
            dto.setSpecialty("N/A");
        }

        // Xử lý an toàn cho Department (Thông qua Staff) ---
        staffOptional.map(Staff::getDepartment)
                .ifPresentOrElse(department -> {
                    dto.setDepartmentId(department.getId());
                    dto.setDepartment(department.getName());
                }, () -> {
                    dto.setDepartmentId(null);
                    dto.setDepartment("N/A");
                });

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
            Doctor doctor = doctorRepo.findByIdWithDetails(doctorId)
                    .orElseThrow(() -> new InvalidInputException("Doctor not found with ID: " + doctorId));
            return covertToResponse(doctor);
        } catch (InvalidInputException e) {
            throw new InvalidInputException("Doctor không tồn tại." + e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            throw new InvalidInputException("Lỗi xử lý dữ liệu bác sĩ (ID: " + doctorId + "). Vui lòng kiểm tra log.");
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
