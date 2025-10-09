package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.staff_position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffPositionRepository extends JpaRepository<staff_position, Integer> {
}
