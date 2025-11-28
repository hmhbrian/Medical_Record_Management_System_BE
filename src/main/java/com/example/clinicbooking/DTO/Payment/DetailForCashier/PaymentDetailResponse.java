package com.example.clinicbooking.DTO.Payment.DetailForCashier;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PaymentDetailResponse {
    private Integer paymentId;
    private String paymentCode;
    private String recordCode;
    private String patientCode;
    private String patientName;
    private BigDecimal totalAmount;
    private BigDecimal insuranceCoverage;
    private BigDecimal patientPaymentTotal;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private List<ItemPaymentDetail> itemPayments;
}
