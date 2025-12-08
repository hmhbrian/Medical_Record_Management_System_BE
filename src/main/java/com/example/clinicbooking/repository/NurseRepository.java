package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NurseRepository extends JpaRepository<Nurse, Integer> {
    // Tìm Nurse theo staffId
    Optional<Nurse> findByStaffId(Integer staffId);
}
