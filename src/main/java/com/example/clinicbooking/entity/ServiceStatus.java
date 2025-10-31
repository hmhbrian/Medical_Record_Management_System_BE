package com.example.clinicbooking.entity;

public enum ServiceStatus {
    PENDING_PAYMENT,// Chờ thanh toán
    PAID,// Đã thanh toán
    IN_PROGRESS,// Đang tiến hành
    COMPLETED,// Hoàn thành
    CANCELLED// Đã hủy
}
