package com.example.clinicbooking.DTO.Payment;

import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordMetricsResponse;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private Integer paymentId;
    private String recordCode;
    private Integer patientId;
    private String patientName;
    private String patientCode;
    private LocalDate patientDateOfBirth;
    private BigDecimal totalAmount;
    private BigDecimal insuranceCoverage;
    private BigDecimal patientPaymentTotal;
    private LocalDateTime createAt;
    private String status;
}
