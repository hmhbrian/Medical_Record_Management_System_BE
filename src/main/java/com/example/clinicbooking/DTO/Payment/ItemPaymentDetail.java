package com.example.clinicbooking.DTO.Payment;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemPaymentDetail {
    private Integer itemId;
    private String serviceType;
    private Integer serviceId;
    private String description;
    private BigDecimal grossAmount;
    private BigDecimal insuranceCoverage;
    private BigDecimal patientPaymentTotal;
}
