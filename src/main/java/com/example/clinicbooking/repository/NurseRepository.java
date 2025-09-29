package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Doctor;
import com.example.clinicbooking.entity.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NurseRepository extends JpaRepository<Nurse, Integer> {
}
