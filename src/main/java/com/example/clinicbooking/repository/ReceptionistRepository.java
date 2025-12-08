package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Receptionist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReceptionistRepository extends JpaRepository<Receptionist, Integer> {
    // Tìm Receptionist theo staffId
    Optional<Receptionist> findByStaffId(Integer staffId);
}
