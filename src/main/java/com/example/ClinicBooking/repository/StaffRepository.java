package com.example.ClinicBooking.repository;

import com.example.ClinicBooking.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, Integer> {
}
