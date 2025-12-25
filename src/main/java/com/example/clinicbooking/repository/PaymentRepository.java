package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.MedicalRecord;
import com.example.clinicbooking.entity.Payment;
import com.example.clinicbooking.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer>, JpaSpecificationExecutor<Payment> {
        List<Payment> findAllByRecord(MedicalRecord record);

        // Tìm phiếu thanh toán theo loại đối tượng và ID đối tượng
        // Ở đây, đối tượng là đơn thuốc ("PRESCRIPTION")
        @Query("SELECT p FROM Payment p JOIN PaymentDetail pd on p.id = pd.payment.id " +
                        "WHERE pd.serviceType = :objectType AND pd.serviceId = :objectId AND p.status = :status")
        Optional<Payment> findByObjectTypeAndObjectIdAndStatus(String objectType, Integer objectId,
                        PaymentStatus status);

        boolean existsByRecordAndStatus(MedicalRecord record, PaymentStatus status);

        // ==================== ADMIN DASHBOARD QUERIES ====================

        /**
         * Tính tổng doanh thu theo tháng (patientPayment, status = PAID)
         * 
         * @param startDateTime Thời điểm bắt đầu tháng
         * @param endDateTime   Thời điểm kết thúc tháng
         * @return Tổng doanh thu trong tháng
         */
        @Query("SELECT COALESCE(SUM(p.patientPayment), 0) FROM Payment p " +
                        "WHERE p.status = 'PAID' " +
                        "AND p.paymentDate BETWEEN :startDateTime AND :endDateTime")
        BigDecimal sumRevenueByDateRange(
                        @Param("startDateTime") LocalDateTime startDateTime,
                        @Param("endDateTime") LocalDateTime endDateTime);

        /**
         * Tính tổng tiền thanh toán của một cuộc hẹn (qua MedicalRecord)
         * 
         * @param appointmentId ID cuộc hẹn
         * @return Tổng tiền totalAmount
         */
        @Query("SELECT COALESCE(SUM(p.totalAmount), 0) FROM Payment p " +
                        "WHERE p.record.appointment.id = :appointmentId")
        BigDecimal sumTotalPaymentByAppointmentId(@Param("appointmentId") Integer appointmentId);

        @Query("SELECT COALESCE(SUM(p.insuranceCoverage), 0) FROM Payment p " +
                        "WHERE p.record.appointment.id = :appointmentId")
        BigDecimal sumInsurancePaymentByAppointmentId(@Param("appointmentId") Integer appointmentId);

        @Query("SELECT COALESCE(SUM(p.patientPayment), 0) FROM Payment p " +
                        "WHERE p.record.appointment.id = :appointmentId")
        BigDecimal sumPatientPaymentByAppointmentId(@Param("appointmentId") Integer appointmentId);

        // ==================== QR PAYMENT QUERIES ====================

        /**
         * Tìm Payment theo paymentCode
         * Được sử dụng bởi SePay webhook để map giao dịch chuyển khoản với phiếu thanh
         * toán
         * 
         * @param paymentCode Mã thanh toán duy nhất (ví dụ: "PAY00123",
         *                    "HD20231210001")
         * @return Payment entity hoặc null nếu không tìm thấy
         */
        Payment findByPaymentCode(String paymentCode);
}
