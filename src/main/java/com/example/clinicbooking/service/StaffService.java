package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.Doctor.DoctorResponse;
import com.example.clinicbooking.DTO.Staff.StaffRequest;
import com.example.clinicbooking.DTO.Staff.StaffResponse;
import com.example.clinicbooking.entity.*;
import com.example.clinicbooking.repository.LabTechnicianRepository;
import com.example.clinicbooking.repository.NurseRepository;
import com.example.clinicbooking.repository.SpecialtyRepository;
import com.example.clinicbooking.repository.UnifiedStaffViewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffService implements IStaffService {
    private NurseRepository nurseRepo;
    private LabTechnicianRepository labTechnicianRepo;
    private SpecialtyRepository specialtyRepo;
    private StaffCreationService staffCreationService;
    private UnifiedStaffViewRepository unifiedRepo;

    public StaffService(NurseRepository nurseRepo,
                         LabTechnicianRepository labTechnicianRepo,
                         SpecialtyRepository specialtyRepo,
                         StaffCreationService staffCreationService,
                         UnifiedStaffViewRepository unifiedRepo) {
        this.unifiedRepo = unifiedRepo;
        this.nurseRepo = nurseRepo;
        this.labTechnicianRepo = labTechnicianRepo;
        this.specialtyRepo = specialtyRepo;
        this.staffCreationService = staffCreationService;
    }
    @Override
    public String create(StaffRequest staffRequest) {
        User user = staffCreationService.createUser(staffRequest);
        Staff staff = staffCreationService.createStaff(user, staffRequest.getDepartmentId(), staffRequest.getPositionId());
        String message = "Tạo nhân viên thành công!";

        if(staffRequest.getPositionId() == 2) {
            Nurse nurse = new Nurse();
            nurse.setStaff(staff);
            nurse.setExperienceYears(staffRequest.experienceYears);
            nurseRepo.save(nurse);
            message = "Thêm y tá thành công!";
        }
        else if(staffRequest.getPositionId() == 3) {
            LabTechnician labTech = new LabTechnician();
            labTech.setStaff(staff);
            labTech.setExperienceYears(staffRequest.experienceYears);
            labTechnicianRepo.save(labTech);
            message = "Thêm kỹ thuật viên phòng thí nghiệm thành công!";
        }

        return message;
    }

    @Override
    public List<String> getAll() {
        return List.of();
    }

    @Override
    public String getbyUserId(Integer userId) {
        return "";
    }

    @Override
    public String update(Integer id, StaffRequest staffRequest) {
        return "";
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public List<StaffResponse> findAll() {
        return unifiedRepo.findAllUnified();
    }

    public Page<StaffResponse> search(
            String roleType, Integer departmentId, Integer positionId, String keyword, Pageable pageable) {
        return unifiedRepo.search(
                (roleType == null || roleType.isBlank()) ? null : roleType,
                departmentId,
                positionId,
                (keyword == null || keyword.isBlank()) ? null : keyword,
                pageable
        );
    }
}
