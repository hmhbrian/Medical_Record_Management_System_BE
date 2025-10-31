package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.PrescriptionDetails;
import com.example.clinicbooking.entity.Prescriptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionDetailRepository extends JpaRepository<PrescriptionDetails, Integer> {

    // Xóa tất cả chi tiết cũ của đơn thuốc
    @Modifying
    @Query("DELETE FROM PrescriptionDetails pd WHERE pd.prescription.id = :prescriptionId")
    void deleteByPrescriptionId(Integer prescriptionId);

    List<PrescriptionDetails> findAllByPrescription(Prescriptions prescription);
}
