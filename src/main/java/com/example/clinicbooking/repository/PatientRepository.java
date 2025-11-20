package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    @Query("""
        select p
        from Patient p
        where p.user.id = :userId
    """)
    Optional<Patient> findByUserId(Integer userId);
    Optional<Patient> findByPatientCodeContainingIgnoreCaseOrUser_PhoneNumberContainingIgnoreCaseOrUser_FullnameContainingIgnoreCase(String patientCode, String phoneNumber, String fullname);
}
