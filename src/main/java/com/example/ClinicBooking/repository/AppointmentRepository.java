package com.example.ClinicBooking.repository;

import com.example.ClinicBooking.entity.Appointment;
import com.example.ClinicBooking.entity.RoomTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByPatientId(int patientId);
    List<Appointment> findByDoctorScheduleId(int doctorScheduleId);
    List<Appointment> findByDoctorId(int doctorId);

    @Query("SELECT a FROM Appointment a WHERE a.doctorSchedule.date BETWEEN :startDate AND :endDate")
    List<Appointment> findByAppointmentDateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT a FROM Appointment a JOIN AppointmentStatus s ON a.id = s.appointment.id " +
            "WHERE s.status = :status " +
            "AND s.updateAt = (SELECT MAX(s2.updateAt) FROM AppointmentStatus s2 WHERE s2.appointment.id = a.id)")
    List<Appointment> findByStatus(@Param("status") String status);
}
