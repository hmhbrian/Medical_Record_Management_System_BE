package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "paymentdetails")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Liên kết tới phiếu Payment tổng
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    // Khóa ngoại tổng quát (Polymorphic Association)
    // Tên bảng đích ('EXAMINATION', 'LAB_TEST', 'IMAGING_TEST', 'PRESCRIPTION', 'BED', 'OTHER')
    @Column(name = "service_type", length = 50)
    private String serviceType;

    // ID của bản ghi trong bảng đích
    @Column(name = "service_id")
    private Integer serviceId;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "amount") //tổng tiền của chi tiết này
    private BigDecimal totalAmount;

    @Column(name = "insurance_covered_amount")
    private BigDecimal insuranceCoveredAmount;

    @Column(name = "patient_paid_amount")
    private BigDecimal patientPaidAmount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
