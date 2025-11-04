package com.example.clinicbooking.DTO.Payment;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PaymentDetailResponse {
    private String recordCode;
    private String patientName;
    private BigDecimal totalAmount;
    private BigDecimal insuranceCoverage;
    private BigDecimal patientPaymentTotal;
    private String status;
    private List<ItemPaymentDetail> itemPayments;
}
