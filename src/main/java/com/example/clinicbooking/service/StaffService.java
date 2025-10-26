package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.Doctor.DoctorResponse;
import com.example.clinicbooking.DTO.Staff.StaffRequest;
import com.example.clinicbooking.DTO.Staff.StaffResponse;
import com.example.clinicbooking.DTO.Staff.StaffSummary;
import com.example.clinicbooking.entity.*;
import com.example.clinicbooking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffService implements IStaffService {
    private final NurseRepository nurseRepo;
    private final ImagingStaffRepository imagingStaffRepo;
    private final CashierRepository cashierRepo;
    private final PharmacyStaffRepository pharmacyStaffRepo;
    private final ReceptionistRepository receptionistRepo;
    private final LabTechnicianRepository labTechnicianRepo;
    private final StaffPositionRepository staffPositionRepo;
    private final StaffCreationService staffCreationService;
    private final UnifiedStaffViewRepository unifiedRepo;

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
        else if(staffRequest.getPositionId() == 4) {
            ImagingStaff imagingStaff = new ImagingStaff();
            imagingStaff.setStaff(staff);
            imagingStaff.setExperienceYears(staffRequest.experienceYears);
            imagingStaffRepo.save(imagingStaff);
            message = "Thêm nhân viên phòng hình ảnh thành công!";
        }
        else if(staffRequest.getPositionId() == 5) {
            PharmacyStaff pharmacyStaff = new PharmacyStaff();
            pharmacyStaff.setStaff(staff);
            pharmacyStaff.setExperienceYears(staffRequest.experienceYears);
            pharmacyStaffRepo.save(pharmacyStaff);
            message = "Thêm nhân viên phát thuốc thành công!";
        }
        else if(staffRequest.getPositionId() == 6) {
            Cashier cashier = new Cashier();
            cashier.setStaff(staff);
            cashier.setExperienceYears(staffRequest.experienceYears);
            cashierRepo.save(cashier);
            message = "Thêm nhân viên thu ngân thành công!";
        }
        else if(staffRequest.getPositionId() == 7) {
            Receptionist receptionist = new Receptionist();
            receptionist.setStaff(staff);
            receptionist.setExperienceYears(staffRequest.experienceYears);
            receptionistRepo.save(receptionist);
            message = "Thêm nhân viên tiếp nhận bệnh nhân thành công!";
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

    @Override
    public ApiResponse<List<StaffSummary>> findStaffByPosition(int positionId) {
        staff_position position = staffPositionRepo.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found with id: " + positionId));
        if(position == null) {
            return new ApiResponse<>(false, "Chức vụ không tồn tại!", null);
        }
        List<StaffSummary> staffList = unifiedRepo.findByPositionId(positionId);
        if(staffList.isEmpty()) {
            return new ApiResponse<>(false, "Không có nhân viên nào thuộc chức vụ: " + position.getPosition(), null);
        }
        return new ApiResponse<>(true, "Lấy danh sách nhân viên theo chức vụ thành công!", staffList);
    }
}
