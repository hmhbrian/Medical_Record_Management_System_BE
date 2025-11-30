package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescriptions, Integer>, JpaSpecificationExecutor<Prescriptions> {
    Optional<Prescriptions> findByRecordAndStatusNotIn(MedicalRecord record, List<PrescriptionStatus> excludedStatuses);
    Optional<Prescriptions> findByRecord(MedicalRecord record);
    boolean existsByRecord(MedicalRecord record);

    // Tổng số đơn trong ngày
    Integer countByPrescriptionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    // Tổng số đơn chờ xử lý (status = PAID) trong ngày
    Integer countByStatusAndPrescriptionDateBetween(PrescriptionStatus status, LocalDateTime startDate, LocalDateTime endDate);
    // Tổng số đơn đang xử lý/hoàn thành (status = PAID) trong ngày của dược sĩ
    Integer countByStatusAndPharmacyStaffAndPrescriptionDateBetween(PrescriptionStatus status, PharmacyStaff pharmacyStaff, LocalDateTime startDate, LocalDateTime endDate);
}
