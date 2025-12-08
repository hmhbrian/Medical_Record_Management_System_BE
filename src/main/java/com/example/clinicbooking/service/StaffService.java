package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.Staff.StaffRequest;
import com.example.clinicbooking.DTO.Staff.StaffResponse;
import com.example.clinicbooking.DTO.Staff.StaffSummary;
import com.example.clinicbooking.entity.*;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffService implements IStaffService {
    private final DoctorRepository doctorRepo;
    private final SpecialtyRepository specialtyRepo;
    private final NurseRepository nurseRepo;
    private final ImagingStaffRepository imagingStaffRepo;
    private final CashierRepository cashierRepo;
    private final PharmacyStaffRepository pharmacyStaffRepo;
    private final ReceptionistRepository receptionistRepo;
    private final LabTechnicianRepository labTechnicianRepo;
    private final StaffPositionRepository staffPositionRepo;
    private final StaffCreationService staffCreationService;
    private final UnifiedStaffViewRepository unifiedRepo;
    private final StaffRepository staffRepo;

    @Override
    public String create(StaffRequest staffRequest) {
        User user = staffCreationService.createUser(staffRequest);
        Staff staff = staffCreationService.createStaff(user, staffRequest.getDepartmentId(),
                staffRequest.getPositionId());
        String message = "Tạo nhân viên thành công!";

        if (staffRequest.getPositionId() == 1) {
            Doctor doctor = new Doctor();
            doctor.setStaff(staff);
            doctor.setExperienceYears(staffRequest.getExperienceYears());
            Specialty specialty = specialtyRepo.findById(staffRequest.getSpecialtyId())
                    .orElseThrow(() -> new InvalidInputException(
                            "Chuyên khoa không tồn tại với id: " + staffRequest.getSpecialtyId()));
            doctor.setSpecialty(specialty);
            doctor.setCertificationName(staffRequest.getCertificationName());
            doctor.setIssuedBy(staffRequest.getIssuedBy());
            doctor.setIssueDate(staffRequest.getIssueDate());
            doctorRepo.save(doctor);
            message = "Thêm bác sĩ thành công!";
        } else if (staffRequest.getPositionId() == 2) {
            Nurse nurse = new Nurse();
            nurse.setStaff(staff);
            nurse.setExperienceYears(staffRequest.experienceYears);
            nurseRepo.save(nurse);
            message = "Thêm y tá thành công!";
        } else if (staffRequest.getPositionId() == 3) {
            LabTechnician labTech = new LabTechnician();
            labTech.setStaff(staff);
            labTech.setExperienceYears(staffRequest.experienceYears);
            labTechnicianRepo.save(labTech);
            message = "Thêm kỹ thuật viên phòng thí nghiệm thành công!";
        } else if (staffRequest.getPositionId() == 4) {
            ImagingStaff imagingStaff = new ImagingStaff();
            imagingStaff.setStaff(staff);
            imagingStaff.setExperienceYears(staffRequest.experienceYears);
            imagingStaffRepo.save(imagingStaff);
            message = "Thêm nhân viên phòng hình ảnh thành công!";
        } else if (staffRequest.getPositionId() == 5) {
            PharmacyStaff pharmacyStaff = new PharmacyStaff();
            pharmacyStaff.setStaff(staff);
            pharmacyStaff.setExperienceYears(staffRequest.experienceYears);
            pharmacyStaffRepo.save(pharmacyStaff);
            message = "Thêm nhân viên phát thuốc thành công!";
        } else if (staffRequest.getPositionId() == 6) {
            Cashier cashier = new Cashier();
            cashier.setStaff(staff);
            cashier.setExperienceYears(staffRequest.experienceYears);
            cashierRepo.save(cashier);
            message = "Thêm nhân viên thu ngân thành công!";
        } else if (staffRequest.getPositionId() == 7) {
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

    /**
     * Cập nhật thông tin nhân viên theo staffId
     * Bao gồm cập nhật User, Staff và entity chuyên môn (Doctor, Nurse, etc.)
     * 
     * @param staffId ID của staff
     * @param request Thông tin cập nhật
     * @return Thông báo kết quả
     */
    @Override
    public String update(Integer staffId, StaffRequest request) {
        // 1. Tìm Staff theo ID
        Staff staff = staffRepo.findById(staffId)
                .orElseThrow(() -> new InvalidInputException("Không tìm thấy nhân viên với ID: " + staffId));

        // 2. Cập nhật User và Staff cơ bản
        staffCreationService.updateUserAndStaff(
                staff.getUser(),
                staff,
                request,
                request.getDepartmentId(),
                request.getPositionId());

        // 3. Cập nhật entity chuyên môn dựa trên positionId
        int positionId = request.getPositionId();
        String message = "Cập nhật nhân viên thành công!";

        if (positionId == 1) {
            // Doctor
            Doctor doctor = doctorRepo.findByStaffId(staffId)
                    .orElseThrow(() -> new InvalidInputException("Không tìm thấy thông tin bác sĩ"));
            doctor.setExperienceYears(request.getExperienceYears());
            if (request.getSpecialtyId() > 0) {
                Specialty specialty = specialtyRepo.findById(request.getSpecialtyId())
                        .orElseThrow(() -> new InvalidInputException(
                                "Chuyên khoa không tồn tại với id: " + request.getSpecialtyId()));
                doctor.setSpecialty(specialty);
            }
            doctor.setCertificationName(request.getCertificationName());
            doctor.setIssuedBy(request.getIssuedBy());
            doctor.setIssueDate(request.getIssueDate());
            doctorRepo.save(doctor);
            message = "Cập nhật bác sĩ thành công!";
        } else if (positionId == 2) {
            // Nurse
            Nurse nurse = nurseRepo.findByStaffId(staffId)
                    .orElseThrow(() -> new InvalidInputException("Không tìm thấy thông tin y tá"));
            nurse.setExperienceYears(request.getExperienceYears());
            nurseRepo.save(nurse);
            message = "Cập nhật y tá thành công!";
        } else if (positionId == 3) {
            // LabTechnician
            LabTechnician labTech = labTechnicianRepo.findByStaffId(staffId)
                    .orElseThrow(() -> new InvalidInputException("Không tìm thấy thông tin kỹ thuật viên"));
            labTech.setExperienceYears(request.getExperienceYears());
            labTechnicianRepo.save(labTech);
            message = "Cập nhật kỹ thuật viên phòng thí nghiệm thành công!";
        } else if (positionId == 4) {
            // ImagingStaff
            ImagingStaff imagingStaff = imagingStaffRepo.findByStaffId(staffId)
                    .orElseThrow(() -> new InvalidInputException("Không tìm thấy thông tin nhân viên hình ảnh"));
            imagingStaff.setExperienceYears(request.getExperienceYears());
            imagingStaffRepo.save(imagingStaff);
            message = "Cập nhật nhân viên phòng hình ảnh thành công!";
        } else if (positionId == 5) {
            // PharmacyStaff
            PharmacyStaff pharmacyStaff = pharmacyStaffRepo.findByStaffId(staffId)
                    .orElseThrow(() -> new InvalidInputException("Không tìm thấy thông tin nhân viên phát thuốc"));
            pharmacyStaff.setExperienceYears(request.getExperienceYears());
            pharmacyStaffRepo.save(pharmacyStaff);
            message = "Cập nhật nhân viên phát thuốc thành công!";
        } else if (positionId == 6) {
            // Cashier
            Cashier cashier = cashierRepo.findByStaffId(staffId)
                    .orElseThrow(() -> new InvalidInputException("Không tìm thấy thông tin thu ngân"));
            cashier.setExperienceYears(request.getExperienceYears());
            cashierRepo.save(cashier);
            message = "Cập nhật nhân viên thu ngân thành công!";
        } else if (positionId == 7) {
            // Receptionist
            Receptionist receptionist = receptionistRepo.findByStaffId(staffId)
                    .orElseThrow(() -> new InvalidInputException("Không tìm thấy thông tin nhân viên tiếp nhận"));
            receptionist.setExperienceYears(request.getExperienceYears());
            receptionistRepo.save(receptionist);
            message = "Cập nhật nhân viên tiếp nhận bệnh nhân thành công!";
        }

        return message;
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
                pageable);
    }

    @Override
    public ApiResponse<List<StaffSummary>> findStaffByPosition(int positionId) {
        staff_position position = staffPositionRepo.findById(positionId)
                .orElseThrow(() -> new InvalidInputException("Position not found with id: " + positionId));
        if (position == null) {
            return new ApiResponse<>(false, "Chức vụ không tồn tại!", null);
        }
        List<StaffSummary> staffList = unifiedRepo.findByPositionId(positionId);
        if (staffList.isEmpty()) {
            return new ApiResponse<>(false, "Không có nhân viên nào thuộc chức vụ: " + position.getPosition(), null);
        }
        return new ApiResponse<>(true, "Lấy danh sách nhân viên theo chức vụ thành công!", staffList);
    }
}
