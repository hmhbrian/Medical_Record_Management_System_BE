package com.example.ClinicBooking.repository;

import com.example.ClinicBooking.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Optional<Patient> findByUserId(Integer integer);
}
