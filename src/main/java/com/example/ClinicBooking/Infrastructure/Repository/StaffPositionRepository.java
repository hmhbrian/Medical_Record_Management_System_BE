package com.example.ClinicBooking.Infrastructure.Repository;

import com.example.ClinicBooking.Domain.Entities.staff_position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffPositionRepository extends JpaRepository<staff_position, Integer> {
}
