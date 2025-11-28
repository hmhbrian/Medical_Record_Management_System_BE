package com.example.clinicbooking.DTO.Payment.DetailForAdmin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceItemDTO {
    private String serviceType;
    private String description;
    private BigDecimal totalAmount;
    private BigDecimal insuranceCoverage;
    private BigDecimal patientPaymentTotal;
}
