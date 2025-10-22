package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.MedicalRecord;
import com.example.clinicbooking.entity.MedicalRecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer>, JpaSpecificationExecutor<MedicalRecord> {

    List<MedicalRecord> findByPatientId(Integer patientId);

    List<MedicalRecord> findByDoctorId(Integer doctorId);

    @Query("SELECT mr FROM MedicalRecord mr GROUP BY mr.patient.id")
    List<MedicalRecord> findAllGroupedByPatient();

    @Query("SELECT COUNT(mr) FROM MedicalRecord mr WHERE mr.doctor.id = :doctorId AND mr.visitDate = :visitDate")
    int countVisitNumber(@Param("doctorId") Integer doctorId, @Param("visitDate") LocalDate visitDate);

    // Truy vấn để đếm tổng số hồ sơ cho một bác sĩ và một ngày
    Integer countByDoctorIdAndVisitDate(Integer doctorId, LocalDate visitDate);

    // Truy vấn để đếm số hồ sơ theo trạng thái
    Integer countByDoctorIdAndVisitDateAndStatus(Integer doctorId, LocalDate visitDate, MedicalRecordStatus status);
}
