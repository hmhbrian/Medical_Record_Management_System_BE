package com.example.clinicbooking.DTO.Payment.DetailForAdmin;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class InvoiceSummaryResponse {
    private Integer paymentId;
    private String paymentStatus; // PAID, PENDING_PAYMENT, CANCELED
    private BigDecimal totalAmount;
    private BigDecimal patientPaid;
    private BigDecimal insuranceCoverage;
    private LocalDateTime paymentDate;
    private String cashierName; // Người thu ngân
    private String cashierCode;
    private List<InvoiceItemDTO> items;
}
