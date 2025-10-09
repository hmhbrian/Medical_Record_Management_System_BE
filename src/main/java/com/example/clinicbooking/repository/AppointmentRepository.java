package com.example.clinicbooking.repository;

import com.example.clinicbooking.DTO.PatientInScheduleResponse;
import com.example.clinicbooking.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer>,
                                                JpaSpecificationExecutor<Appointment> {
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
    List<Appointment> findByStatus(@Param("status") int status);

    @Query("""
        select new com.example.clinicbooking.DTO.PatientInScheduleResponse(
            p.id, p.user.fullname
        )
        from Appointment a
        join a.patient p
        where a.doctorSchedule.id = :scheduleId
        order by p.user.fullname
    """)
    List<PatientInScheduleResponse> findPatientByDoctorScheduleId(@Param("scheduleId") int doctorScheduleId);
}
