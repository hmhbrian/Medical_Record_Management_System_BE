package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppointmentStatusRepository extends JpaRepository<AppointmentStatus, Integer> {
    Optional<AppointmentStatus> findTopByAppointmentIdOrderByUpdateAtDesc(int id);

}
