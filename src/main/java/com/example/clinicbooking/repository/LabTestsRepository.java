package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.LabTests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabTestsRepository extends JpaRepository<LabTests, Integer> {
}
