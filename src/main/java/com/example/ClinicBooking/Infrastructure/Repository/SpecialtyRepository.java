package com.example.ClinicBooking.Infrastructure.Repository;

import com.example.ClinicBooking.Domain.Entities.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecialtyRepository extends JpaRepository<Specialty, Integer> {
    List<Specialty> findSpecialtiesByDepartment_Id(int departmentId);
}
