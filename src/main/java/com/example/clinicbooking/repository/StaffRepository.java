package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Integer> {
    Optional<Staff> findByUserId(int userId);
}
