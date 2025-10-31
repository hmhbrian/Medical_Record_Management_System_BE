package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.MedicalRecord;
import com.example.clinicbooking.entity.Prescriptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescriptions, Integer> {
    Optional<Prescriptions> findByRecord(MedicalRecord record);
}
