package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.Dashboard.AdminDashboardOverviewDTO;
import com.example.clinicbooking.DTO.Dashboard.DashboardOverview;
import com.example.clinicbooking.DTO.Dashboard.WeeklyStatisticsDTO;
import com.example.clinicbooking.DTO.Dashboard.Doctor.AppointmentUpcomingDTO;
import com.example.clinicbooking.DTO.Dashboard.Doctor.PatientOverview;
import com.example.clinicbooking.security.CustomUserDetails;
import com.example.clinicbooking.service.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Quản lý bảng điều khiển")
public class DashboardController {
    private final DashboardService dashboardService;

    // LabStaff dashboard
    @GetMapping("/lab-overview")
    public DashboardOverview getLabStaffDashboard(@RequestParam(required = false) String searchDate) {
        // Lấy id User đang đăng nhập
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }
        Integer currentUserId = cud.getId();

        return dashboardService.getLabStaffDashboardSummary(searchDate, currentUserId);
    }

    // Pharmacist dashboard
    @GetMapping("/pharmacist-overview")
    public DashboardOverview getPharmacistDashboard(@RequestParam(required = false) String searchDate) {
        // Lấy id User đang đăng nhập
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }
        Integer currentUserId = cud.getId();

        return dashboardService.getPharmacistDashboardSummary(searchDate, currentUserId);
    }

    // Doctor dashboard
    @GetMapping("/doctor-overview")
    public PatientOverview getDoctorOverview(@RequestParam(required = false) String searchDate) {
        // Lấy id User đang đăng nhập
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }
        Integer currentUserId = cud.getId();

        return dashboardService.getPatientOverviewForDoctor(searchDate, currentUserId);
    }

    // Doctor - Danh sách lịch hẹn sắp tới
    @GetMapping("/doctor-appointments-upcoming")
    public List<AppointmentUpcomingDTO> getUpcomingAppointmentsForDoctor() {
        // Lấy id User đang đăng nhập
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }
        Integer currentUserId = cud.getId();

        return dashboardService.getUpcomingAppointmentForDoctor(currentUserId);
    }

    // ==================== ADMIN DASHBOARD ====================

    /**
     * Lấy tổng quan Dashboard Admin
     * Endpoint: GET /api/dashboard/admin-overview
     * 
     * @param searchDate Ngày cần thống kê (định dạng yyyy-MM-dd), không bắt buộc
     * @return AdminDashboardOverviewDTO chứa các thống kê tổng quan Admin
     */
    @GetMapping("/admin-overview")
    public AdminDashboardOverviewDTO getAdminDashboardOverview(
            @RequestParam(required = false) String searchDate) {
        return dashboardService.getAdminDashboardOverview(searchDate);
    }

    /**
     * Lấy thống kê theo tuần
     * Endpoint: GET /api/dashboard/weekly-statistics
     * 
     * @param weekStartDate Ngày bắt đầu tuần (định dạng yyyy-MM-dd), không bắt buộc
     * @return WeeklyStatisticsDTO chứa thống kê theo tuần
     */
    @GetMapping("/weekly-statistics")
    public WeeklyStatisticsDTO getWeeklyStatistics(
            @RequestParam(required = false) String weekStartDate) {
        return dashboardService.getWeeklyStatistics(weekStartDate);
    }
}
