package com.example.ClinicBooking.repository;

import com.example.ClinicBooking.entity.staff_position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffPositionRepository extends JpaRepository<staff_position, Integer> {
}
