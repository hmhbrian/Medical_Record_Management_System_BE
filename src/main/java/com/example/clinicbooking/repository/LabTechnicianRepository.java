package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Doctor;
import com.example.clinicbooking.entity.LabTechnician;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabTechnicianRepository extends JpaRepository<LabTechnician, Integer> {
}
