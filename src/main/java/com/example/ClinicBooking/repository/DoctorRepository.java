package com.example.ClinicBooking.repository;

import com.example.ClinicBooking.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
}
