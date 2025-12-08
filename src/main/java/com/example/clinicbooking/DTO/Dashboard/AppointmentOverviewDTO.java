package com.example.clinicbooking.DTO.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho tổng quan quản lý lịch hẹn
 * Được sử dụng trong Dashboard cho Admin/Lễ tân
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentOverviewDTO {

    /**
     * Tổng số cuộc hẹn (tất cả thời gian)
     */
    private Long totalAppointments;

    /**
     * Thông tin áp lực phòng chờ (theo ngày)
     */
    private WaitingRoomPressure waitingRoomPressure;

    /**
     * Số lượng lịch hẹn chờ xác nhận (status = 1, tất cả)
     */
    private Long pendingConfirmation;

    /**
     * Số lượng lịch hẹn đã hủy (status = 6, tất cả)
     */
    private Long cancelledCount;

    /**
     * Số lượng lịch hẹn đã hoàn thành trong ngày (status = 5)
     */
    private Long completedToday;

    /**
     * Thống kê loại bệnh nhân trong ngày (hẹn trước / vãng lai)
     */
    private PatientTypeToday patientTypeToday;

    /**
     * DTO cho thông tin áp lực phòng chờ
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WaitingRoomPressure {
        /**
         * Tổng số người có mặt tại bệnh viện trong ngày
         * (những người đã check-in, có presentTime)
         */
        private Long totalPresentAtHospital;

        /**
         * Số người đang chờ khám (status = 3)
         */
        private Long waitingCount;

        /**
         * Số người đang khám (status = 4)
         */
        private Long inProgressCount;
    }

    /**
     * DTO cho thống kê loại bệnh nhân trong ngày
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PatientTypeToday {
        /**
         * Số lượng khách hẹn trước (visitType = 'scheduled')
         */
        private Long scheduled;

        /**
         * Số lượng khách vãng lai (visitType = 'walk-in')
         */
        private Long walkIn;
    }
}
