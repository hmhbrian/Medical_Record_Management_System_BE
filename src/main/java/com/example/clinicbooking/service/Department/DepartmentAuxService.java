package com.example.clinicbooking.service.Department;

import com.example.clinicbooking.entity.Specialty;
import com.example.clinicbooking.repository.SpecialtyRepository;
import com.example.clinicbooking.repository.StaffRepository;
import com.example.clinicbooking.repository.roomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentAuxService {
    private final StaffRepository staffRepository;
    private final SpecialtyRepository specialtyRepository;
    private final roomRepository roomRepository;

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
        return specialtyRepository.findSpecialtiesByDepartment_Id(deptId);
    }
}
