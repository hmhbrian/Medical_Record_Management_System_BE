package com.example.ClinicBooking.repository;

import com.example.ClinicBooking.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppointmentStatusRepository extends JpaRepository<AppointmentStatus, Integer> {
    Optional<AppointmentStatus> findTopByAppointmentIdOrderByUpdateAtDesc(int id);

}
