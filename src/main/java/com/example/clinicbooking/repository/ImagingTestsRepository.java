package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.ImagingTests;
import com.example.clinicbooking.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImagingTestsRepository extends JpaRepository<ImagingTests, Integer> {
    List<ImagingTests> findAllByRecord(MedicalRecord record);
}
