package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Department;
import com.example.clinicbooking.entity.DrugType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrugTypeRepository extends JpaRepository<DrugType, Integer> {
}
