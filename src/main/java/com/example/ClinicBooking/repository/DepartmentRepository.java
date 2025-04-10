package com.example.ClinicBooking.repository;

import com.example.ClinicBooking.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
