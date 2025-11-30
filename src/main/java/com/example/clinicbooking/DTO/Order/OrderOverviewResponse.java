package com.example.clinicbooking.DTO.Order;

import lombok.Data;

@Data
public class OrderOverviewResponse {
    private long totalPendingOrders;   // Tổng số y lệnh đang chờ xử lý
    private long totalOverdueOrders;   // Tổng số y lệnh quá hạn
    private long totalCompletedToday;  // Tổng số y lệnh hoàn thành trong ngày
    private double averageTAT;         // Thời gian Xử lý Trung bình
}
