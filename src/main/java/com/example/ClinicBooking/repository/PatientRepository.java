package com.example.ClinicBooking.repository;

import com.example.ClinicBooking.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
}
