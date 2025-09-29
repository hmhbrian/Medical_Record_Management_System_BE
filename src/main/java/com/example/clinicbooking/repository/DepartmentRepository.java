package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
