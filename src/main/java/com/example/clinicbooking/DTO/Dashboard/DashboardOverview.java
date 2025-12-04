package com.example.clinicbooking.DTO.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardOverview {
    private Integer totalToday;// Tổng đơn thuốc/ xét nghiệm hôm nay
    private Integer pendingForDispensing;   // Chờ xử lý
    private Integer inProgressByMe;        // Đang xử lý bởi nv
    private Integer completedByMe;         // Đã hoàn thành bởi nv
}
