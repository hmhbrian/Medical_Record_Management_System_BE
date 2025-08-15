package com.example.ClinicBooking.Infrastructure.Repository;

import com.example.ClinicBooking.Domain.Entities.PrescriptionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionDetailRepository extends JpaRepository<PrescriptionDetail, Integer> {
    List<PrescriptionDetail> findByPrescriptionId(Integer prescriptionId);
}
