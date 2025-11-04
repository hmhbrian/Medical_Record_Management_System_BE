package com.example.clinicbooking.DTO.Payment;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentProcessRequest {
    private String paymentMethod;       // Ví dụ: "CASH", "CARD", "TRANSFER"
    private BigDecimal actualPaidAmount;  // Số tiền thực tế bệnh nhân đã trả (dùng cho trường hợp trả thừa/thiếu)
}
