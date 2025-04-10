package com.example.ClinicBooking.repository;

import com.example.ClinicBooking.entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecialtyRepository extends JpaRepository<Specialty, Integer> {
    List<Specialty> findSpecialtiesByDepartment_Id(int departmentId);
}
