package com.example.ClinicBooking.repository;

import com.example.ClinicBooking.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Integer> {
    Optional<Staff> findByUserId(int userId);
}
