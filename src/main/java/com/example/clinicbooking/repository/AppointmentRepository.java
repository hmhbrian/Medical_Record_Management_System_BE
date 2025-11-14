package com.example.clinicbooking.repository;

import com.example.clinicbooking.DTO.PatientInScheduleResponse;
import com.example.clinicbooking.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer>,
                                                JpaSpecificationExecutor<Appointment> {
    List<Appointment> findByPatientId(int patientId);
    List<Appointment> findByDoctorScheduleId(int doctorScheduleId);
    List<Appointment> findByDoctorId(int doctorId);
    List<Appointment> findByDoctorIdAndDoctorSchedule_DateEqualsOrderByScheduleSlotAsc(Integer doctorId, LocalDate date);

    @Query("SELECT a FROM Appointment a " +
            "JOIN FETCH a.patient p " +
            "JOIN FETCH a.doctor d " +
            "JOIN FETCH d.staff s " +
            "JOIN FETCH s.user us " +
            "JOIN FETCH p.user up " +
            "WHERE a.id = :id")
    Optional<Appointment> findByIdWithDetails(@Param("id") int appointmentId);

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
        join Patient p on a.patient.id = p.id
            join AppointmentStatus s on a.id = s.appointment.id
        where a.doctorSchedule.id = :scheduleId AND s.status < 4
        order by p.user.fullname
    """)
    List<PatientInScheduleResponse> findPatientByDoctorScheduleId(@Param("scheduleId") int doctorScheduleId);



    // Truy vấn để đếm số lần khám cho một bác sĩ trong một ngày
//    @Query("SELECT COUNT(a) FROM Appointment a " +
//            "WHERE a.doctor.id = :doctorId AND FUNCTION('DATE',a.visitDateTime) = :visitDate")
    @Query("SELECT COUNT(a) FROM Appointment a " +
           "WHERE a.doctor.id = :doctorId AND a.doctorSchedule.id = :doctorScheduleId")
    Integer countVisitNumber(@Param("doctorId") Integer doctorId, @Param("doctorScheduleId") Integer doctorScheduleId);
}
