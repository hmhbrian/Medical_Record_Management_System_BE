package com.example.ClinicBooking.repository;

import com.example.ClinicBooking.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    List<Doctor> findBySpecialtyId(Integer specialty);
}
