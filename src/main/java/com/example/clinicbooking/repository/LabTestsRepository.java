package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.LabTests;
import com.example.clinicbooking.entity.MedicalRecord;
import com.example.clinicbooking.entity.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface LabTestsRepository extends JpaRepository<LabTests, Integer>, JpaSpecificationExecutor<LabTests> {
    List<LabTests> findAllByRecord(MedicalRecord record);
    Integer countByRecordAndStatusIn(MedicalRecord record, Collection<ServiceStatus> statuses);
}
