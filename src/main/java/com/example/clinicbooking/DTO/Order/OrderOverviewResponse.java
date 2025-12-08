package com.example.clinicbooking.DTO.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho tổng quan quản lý y lệnh
 * Chứa các thống kê theo tiêu chí Admin yêu cầu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderOverviewResponse {
    /**
     * Tổng số y lệnh (tất cả thời gian)
     */
    private Long totalOrders;

    /**
     * Tổng số y lệnh trong ngày (theo requestedAt)
     */
    private Long totalOrdersToday;

    /**
     * Số y lệnh chờ xử lý trong ngày (status = PAID)
     */
    private Long pendingOrdersToday;

    /**
     * Số y lệnh đang thực hiện trong ngày (status = IN_PROGRESS)
     */
    private Long inProgressOrdersToday;

    /**
     * Số y lệnh hoàn thành trong ngày (status = COMPLETED)
     */
    private Long completedOrdersToday;
}
