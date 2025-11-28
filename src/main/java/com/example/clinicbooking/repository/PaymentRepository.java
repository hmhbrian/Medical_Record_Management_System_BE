package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.MedicalRecord;
import com.example.clinicbooking.entity.Payment;
import com.example.clinicbooking.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer>, JpaSpecificationExecutor<Payment> {
    List<Payment> findAllByRecord(MedicalRecord record);

    // Tìm phiếu thanh toán theo loại đối tượng và ID đối tượng
    // Ở đây, đối tượng là đơn thuốc ("PRESCRIPTION")
    @Query("SELECT p FROM Payment p JOIN PaymentDetail pd on p.id = pd.payment.id " +
            "WHERE pd.serviceType = :objectType AND pd.serviceId = :objectId AND p.status = :status")
    Optional<Payment> findByObjectTypeAndObjectIdAndStatus(String objectType, Integer objectId, PaymentStatus status);

    boolean existsByRecordAndStatus(MedicalRecord record, PaymentStatus status);
}
