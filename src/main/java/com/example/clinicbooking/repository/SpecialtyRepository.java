package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecialtyRepository extends JpaRepository<Specialty, Integer> {
    List<Specialty> findSpecialtiesByDepartment_Id(int departmentId);
    int countSpecialtyByDepartment_Id(int departmentId);
}
