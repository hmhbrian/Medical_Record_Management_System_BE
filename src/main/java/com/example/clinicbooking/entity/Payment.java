package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Liên kết tới Hồ sơ Bệnh án
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "record_id")
    private MedicalRecord record;

    // Liên kết tới Nhân viên Thu ngân
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cashier_id")
    private Cashier cashier; //

    @Column(name = "total")
    private BigDecimal totalAmount; // Tổng tiền trước BHYT

    @Column(name = "insurance_coverage")
    private BigDecimal insuranceCoverage; // Tổng tiền BHYT chi trả

    @Column(name = "patient_payment")
    private BigDecimal patientPayment; // Tổng tiền bệnh nhân thanh toán

    @Column(name = "payment_date")
    private LocalDateTime paymentDate; // Sử dụng LocalDateTime cho ngày giờ thanh toán

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "status", length = 50,nullable = false)
    @Enumerated(EnumType.STRING) // Dùng Enum cho trạng thái để dễ quản lý
    private PaymentStatus status; // Ví dụ: PENDING_PAYMENT, PAID, CANCELED

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "actual_paid_amount")
    private BigDecimal actualPaidAmount;
    @Column(name = "invoice_number")
    private String InvoiceNumber;
    @Column(name = "invoice_serial")
    private String InvoiceSerial;
    @Column(name = "is_invoice_issued")
    private Boolean IsInvoiceIssued;

    private String notes;
}
