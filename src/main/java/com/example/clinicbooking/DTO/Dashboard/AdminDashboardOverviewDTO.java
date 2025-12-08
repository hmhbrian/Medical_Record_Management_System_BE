package com.example.clinicbooking.DTO.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO cho tổng quan Dashboard Admin
 * Chứa các thống kê tổng hợp cho trang chủ Admin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardOverviewDTO {

    /**
     * Tổng số bệnh nhân (tất cả thời gian)
     */
    private Long totalPatients;

    /**
     * Số bệnh nhân mới đăng ký hôm nay
     */
    private Long newPatientsToday;

    /**
     * Số lịch hẹn diễn ra hôm nay (theo doctorSchedule.date, visitType =
     * 'scheduled')
     */
    private Long appointmentsToday;

    /**
     * Số lịch hẹn chờ xác nhận (đặt hôm nay, status = 1)
     * Dựa vào trường presentTime để xác định ngày đặt
     */
    private Long pendingConfirmation;

    /**
     * Doanh thu tháng hiện tại (tổng patientPayment, status = PAID)
     */
    private BigDecimal monthlyRevenue;

    /**
     * Tổng số nhân viên
     */
    private Long totalStaff;

    /**
     * Số thuốc sắp hết (current_quantity <= minimum_quantity)
     */
    private Long lowStockMedicines;

    /**
     * Số bệnh nhân vãng lai hôm nay (visitType = 'walk-in')
     */
    private Long walkInPatientsToday;

    /**
     * Số bệnh nhân hoàn thành hôm nay (status = 5)
     */
    private Long completedPatientsToday;
}
