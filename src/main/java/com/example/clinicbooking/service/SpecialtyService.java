package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.Specialty.SpecialtyRequest;
import com.example.clinicbooking.DTO.Specialty.SpecialtyResponse;
import com.example.clinicbooking.entity.Department;
import com.example.clinicbooking.entity.Specialty;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.DepartmentRepository;
import com.example.clinicbooking.repository.DoctorRepository;
import com.example.clinicbooking.repository.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpecialtyService {
    private final SpecialtyRepository specialtyRepository;
    private final DepartmentRepository departmentRepo;
    private final DoctorRepository doctorRepo;

    // TẠO MỚI CHUYÊN KHOA
    public Specialty save(SpecialtyRequest specialtyRq) {
        Department dept = departmentRepo.findById(specialtyRq.getDepartmentId())
                .orElseThrow(() -> new InvalidInputException(
                        "Department not found with id: " + specialtyRq.getDepartmentId()));

        Specialty specialty = new Specialty();
        specialty.setName(specialtyRq.getName());
        specialty.setDescription(specialtyRq.getDescription());
        specialty.setIcon(specialtyRq.getIcon());
        specialty.setDepartment(dept);
        return specialtyRepository.save(specialty);
    }

    // CẬP NHẬT CHUYÊN KHOA
    public Specialty update(int id, SpecialtyRequest specialtyRq) {
        Specialty specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new InvalidInputException("Specialty not found with id: " + id));

        // Nếu request có departmentId thì lấy ra department
        if (specialtyRq.getDepartmentId() > 0) {
            Department department = departmentRepo.findById(specialtyRq.getDepartmentId())
                    .orElseThrow(() -> new InvalidInputException(
                            "Department not found with id: " + specialtyRq.getDepartmentId()));
            specialty.setDepartment(department);
        }

        // Cập nhật các field khác
        specialty.setName(specialtyRq.getName());
        specialty.setDescription(specialtyRq.getDescription());
        specialty.setIcon(specialtyRq.getIcon());

        return specialtyRepository.save(specialty);
    }

    // XÓA CHUYÊN KHOA THEO ID
    public void deleteSpecialtyById(int id) {
        specialtyRepository.deleteById(id);
    }

    // LẤY TẤT CẢ CHUYÊN KHOA
    public List<SpecialtyResponse> getAll() {
        return specialtyRepository.findAll().stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    // LẤY CHUYÊN KHOA THEO ID
    public Specialty getSpecialtyById(int id) {
        Specialty specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new InvalidInputException("Không tìm thấy chuyên khoa"));
        return specialty;
    }

    // LẤY DANH SÁCH CHUYÊN KHOA THEO DEPARTMENT ID
    public List<SpecialtyResponse> getSpecialtiesByDepartment(int departmentId) {
        Department department = departmentRepo.findById(departmentId)
                .orElseThrow(() -> new InvalidInputException("Department not found with id: " + departmentId));

        List<Specialty> specialties = specialtyRepository.findSpecialtiesByDepartment(department);
        return specialties.stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    // Chuyển đổi entity Specialty sang DTO SpecialtyResponse
    private SpecialtyResponse covertToResponse(Specialty specialty) {
        SpecialtyResponse dto = new SpecialtyResponse();
        dto.setId(specialty.getId());
        dto.setName(specialty.getName());
        dto.setDescription(specialty.getDescription());
        dto.setDepartmentId(specialty.getDepartment().getId());
        dto.setDepartmentName(specialty.getDepartment().getName());
        dto.setIcon(specialty.getIcon());

        // số lượng bác sĩ theo specialty
        int countDoctors = doctorRepo.countBySpecialtyId(specialty.getId());
        dto.setNumberOfDoctors(countDoctors);
        return dto;
    }
}
