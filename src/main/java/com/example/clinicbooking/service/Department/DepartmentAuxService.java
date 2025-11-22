package com.example.clinicbooking.service.Department;

import com.example.clinicbooking.entity.Department;
import com.example.clinicbooking.entity.Specialty;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.DepartmentRepository;
import com.example.clinicbooking.repository.SpecialtyRepository;
import com.example.clinicbooking.repository.StaffRepository;
import com.example.clinicbooking.repository.roomRepository;
import com.example.clinicbooking.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentAuxService {
    private final StaffRepository staffRepository;
    private final SpecialtyRepository specialtyRepository;
    private final roomRepository roomRepository;
    private final DepartmentRepository departmentRepository;

    public int staffCount(int deptId) {
        return staffRepository.countStaffByDepartmentId(deptId);
    }

    public int specialtyCount(int deptId) {
        return specialtyRepository.countSpecialtyByDepartment_Id(deptId);
    }

    public int roomCount(int deptId) {
        return roomRepository.countByDepartmentId(deptId);
    }

    public List<Specialty> specialtiesOf(int deptId) {
        Department department = departmentRepository.findById(deptId).orElseThrow(
                () -> new InvalidInputException("Department not found with id: " + deptId)
        );
        return specialtyRepository.findSpecialtiesByDepartment(department);
    }
}
