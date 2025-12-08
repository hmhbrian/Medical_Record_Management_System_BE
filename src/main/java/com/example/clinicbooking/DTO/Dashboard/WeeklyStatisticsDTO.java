package com.example.clinicbooking.DTO.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO cho thống kê theo tuần
 * Trả về thông tin bệnh nhân và doanh thu theo từng ngày trong tuần
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyStatisticsDTO {

    /**
     * Ngày bắt đầu tuần (Thứ Hai)
     */
    private LocalDate weekStartDate;

    /**
     * Ngày kết thúc tuần (Chủ Nhật)
     */
    private LocalDate weekEndDate;

    /**
     * Danh sách thống kê theo từng ngày trong tuần
     */
    private List<DailyStatistic> dailyStatistics;

    /**
     * Tổng số bệnh nhân cả tuần
     */
    private Long totalPatientsWeek;

    /**
     * Tổng doanh thu cả tuần
     */
    private BigDecimal totalRevenueWeek;

    /**
     * DTO cho thống kê theo ngày
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DailyStatistic {
        /**
         * Thứ trong tuần (tiếng Anh): MONDAY, TUESDAY, ...
         */
        private String dayOfWeek;

        /**
         * Thứ trong tuần (tiếng Việt): Thứ Hai, Thứ Ba, ...
         */
        private String dayOfWeekVi;

        /**
         * Ngày cụ thể
         */
        private LocalDate date;

        /**
         * Số bệnh nhân khám trong ngày
         */
        private Long patientCount;

        /**
         * Tổng doanh thu trong ngày
         */
        private BigDecimal totalRevenue;
    }
}
