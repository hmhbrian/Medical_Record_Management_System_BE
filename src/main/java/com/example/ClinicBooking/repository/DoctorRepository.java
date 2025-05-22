package com.example.ClinicBooking.repository;

import com.example.ClinicBooking.entity.Doctor;
import com.example.ClinicBooking.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    List<Doctor> findBySpecialtyId(Integer specialty);
    Optional<Doctor> findById(Integer doctorId);
}
