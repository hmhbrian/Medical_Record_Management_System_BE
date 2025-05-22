package com.example.ClinicBooking.repository;

import com.example.ClinicBooking.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer> {

    List<MedicalRecord> findByPatientId(Integer patientId);

    List<MedicalRecord> findByDoctorId(Integer doctorId);

    @Query("SELECT mr FROM MedicalRecord mr GROUP BY mr.patient.id")
    List<MedicalRecord> findAllGroupedByPatient();

    @Query("SELECT COUNT(mr) FROM MedicalRecord mr WHERE mr.doctor.id = :doctorId AND mr.visitDate = :visitDate")
    int countVisitNumber(@Param("doctorId") Integer doctorId, @Param("visitDate") LocalDate visitDate);
}
