package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.LabTests;
import com.example.clinicbooking.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabTestsRepository extends JpaRepository<LabTests, Integer> {
    List<LabTests> findAllByRecord(MedicalRecord record);
}
