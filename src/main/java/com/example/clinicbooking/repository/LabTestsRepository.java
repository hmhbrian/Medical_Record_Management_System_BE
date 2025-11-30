package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface LabTestsRepository extends JpaRepository<LabTests, Integer>, JpaSpecificationExecutor<LabTests> {
    List<LabTests> findAllByRecord(MedicalRecord record);
    Integer countByRecordAndStatusIn(MedicalRecord record, Collection<ServiceStatus> statuses);
    boolean existsByRecord(MedicalRecord record);

    // Tổng số xét nghiệm trong ngày
    Integer countByRequestedDateBetween(LocalDateTime requestedDateAfter, LocalDateTime requestedDateBefore);
    // Tổng số đơn chờ xử lý (status = PAID) trong ngày
    Integer countByStatusAndRequestedDateBetween(ServiceStatus status, LocalDateTime requestedDateAfter, LocalDateTime requestedDateBefore);
    // Tổng số đơn đang xử lý/hoàn thành (status = PAID) trong ngày của nv xét nghiệm
    Integer countByStatusAndLabTechnicianAndRequestedDateBetween(ServiceStatus status, LabTechnician labTechnician, LocalDateTime requestedDateAfter, LocalDateTime requestedDateBefore);

}
