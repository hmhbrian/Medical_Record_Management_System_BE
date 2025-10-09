package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.Department.DepartmentRequest;
import com.example.clinicbooking.DTO.Specialty.SpecialtyRequest;
import com.example.clinicbooking.DTO.Specialty.SpecialtyResponse;
import com.example.clinicbooking.entity.Department;
import com.example.clinicbooking.entity.Doctor;
import com.example.clinicbooking.entity.Medicine;
import com.example.clinicbooking.entity.Specialty;
import com.example.clinicbooking.repository.DepartmentRepository;
import com.example.clinicbooking.repository.DoctorRepository;
import com.example.clinicbooking.repository.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpecialtyService {
    private final SpecialtyRepository specialtyRepository;
    private final DepartmentRepository departmentRepo;
    private final DoctorRepository doctorRepo;

    public List<SpecialtyResponse> getAll() {
        return specialtyRepository.findAll().stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    private SpecialtyResponse covertToResponse(Specialty specialty) {
        SpecialtyResponse dto = new SpecialtyResponse();
        dto.setId(specialty.getId());
        dto.setName(specialty.getName());
        dto.setDescription(specialty.getDescription());
        dto.setIcon(specialty.getIcon());

        //số lượng bác sĩ theo specialty
        int countDoctors = doctorRepo.countBySpecialtyId(specialty.getId());
        dto.setNumberOfDoctors(countDoctors);
        return dto;
    }

    public Specialty getSpecialtyById(int id) {
        Specialty specialty = specialtyRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Không tìm thấy chuyên khoa"));
        return specialty;
    }

    public List<Specialty> getSpecialtiesByDepartment(int departmentId) {
        return specialtyRepository.findSpecialtiesByDepartment_Id(departmentId);
    }

    public Specialty save(SpecialtyRequest specialtyRq) {
        Department dept = departmentRepo.findById(specialtyRq.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + specialtyRq.getDepartmentId()));

        Specialty specialty = new Specialty();
        specialty.setName(specialtyRq.getName());
        specialty.setDescription(specialtyRq.getDescription());
        specialty.setIcon(specialtyRq.getIcon());
        specialty.setDepartment(dept);
        return specialtyRepository.save(specialty);
    }

    public Specialty update(int id, SpecialtyRequest specialtyRq) {
        Specialty specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Specialty not found with id: " + id));

        // Nếu request có departmentId thì lấy ra department
        if (specialtyRq.getDepartmentId() > 0) {
            Department department = departmentRepo.findById(specialtyRq.getDepartmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + specialtyRq.getDepartmentId()));
            specialty.setDepartment(department);
        }

        // Cập nhật các field khác
        specialty.setName(specialtyRq.getName());
        specialty.setDescription(specialtyRq.getDescription());
        specialty.setIcon(specialtyRq.getIcon());

        return specialtyRepository.save(specialty);
    }

    public void deleteSpecialtyById(int id) {
        specialtyRepository.deleteById(id);
    }
}
