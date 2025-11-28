package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Appointment;
import com.example.clinicbooking.entity.MedicalRecord;
import com.example.clinicbooking.entity.MedicalRecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer>, JpaSpecificationExecutor<MedicalRecord> {

    List<MedicalRecord> findByPatientId(Integer patientId);

    List<MedicalRecord> findByDoctorId(Integer doctorId);

    @Query("SELECT mr FROM MedicalRecord mr GROUP BY mr.patient.id")
    List<MedicalRecord> findAllGroupedByPatient();

//    @Query("SELECT COUNT(mr) FROM MedicalRecord mr WHERE mr.doctor.id = :doctorId AND mr.visitDate = :visitDate")
//    int countVisitNumber(@Param("doctorId") Integer doctorId, @Param("visitDate") LocalDate visitDate);

    // Truy vấn để đếm tổng số hồ sơ cho một bác sĩ và một ngày
    @Query("SELECT COUNT(mr) FROM MedicalRecord mr join Appointment a on mr.appointment.id = a.id " +
            "WHERE mr.doctor.id = :doctorId AND FUNCTION('DATE',a.visitDateTime) = :visitDate")
    Integer countByDoctorIdAndVisitDate(Integer doctorId, LocalDate visitDate);

    // Truy vấn để đếm số hồ sơ theo trạng thái
    @Query("SELECT COUNT(mr) FROM MedicalRecord mr join Appointment a on mr.appointment.id = a.id " +
            "WHERE mr.doctor.id = :doctorId " +
            "AND FUNCTION('DATE',a.visitDateTime) = :visitDate"+
            " AND mr.status = :status")
    Integer countByDoctorIdAndVisitDateAndStatus(Integer doctorId, LocalDate visitDate, MedicalRecordStatus status);

    //Query fetch join để tối ưu hiệu suất nếu cần lấy Patient/User/Appointment cùng lúc.
    @Query("SELECT mr FROM MedicalRecord mr JOIN FETCH mr.patient p JOIN FETCH p.user u WHERE mr.id = :recordId")
    Optional<MedicalRecord> findByIdWithPatientAndUser(Integer recordId);

    Optional<MedicalRecord> findByAppointment(Appointment appointment);

    @Query("SELECT mr FROM MedicalRecord mr " +
            "JOIN FETCH mr.patient p " +
            "JOIN FETCH mr.doctor d " +
            "WHERE mr.id = :recordId")
    Optional<MedicalRecord> findDetailById(Integer recordId);
}
