package com.example.clinicbooking.entity;

public enum PrescriptionStatus {
    DRAFT, // Bản nháp
    PENDING_PAYMENT, // Chờ thanh toán
    PAID, // Đã thanh toán/Chờ cấp
    COMPLETED, // Đã hoàn thành
    CANCELED // Đã hủy
}
