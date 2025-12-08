package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.Dashboard.AdminDashboardOverviewDTO;
import com.example.clinicbooking.DTO.Dashboard.DashboardOverview;
import com.example.clinicbooking.DTO.Dashboard.WeeklyStatisticsDTO;
import com.example.clinicbooking.DTO.Dashboard.Doctor.AppointmentUpcomingDTO;
import com.example.clinicbooking.DTO.Dashboard.Doctor.PatientOverview;
import com.example.clinicbooking.entity.*;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DashboardService {
    private final LabTestsRepository labTestRepo;
    private final LabTechnicianRepository labTechnicianRepo;

    private final PrescriptionRepository prescriptionRepo;
    private final PharmacyStaffRepository pharmacyStaffRepo;

    private final DoctorRepository doctorRepo;
    private final AppointmentRepository appointmentRepo;

    // Admin Dashboard repositories
    private final PatientRepository patientRepo;
    private final PaymentRepository paymentRepo;
    private final MedicineRepository medicineRepo;
    private final StaffRepository staffRepo;

    // Lấy Tổng quan Dashboard cho Nhân viên xét nghiệm
    public DashboardOverview getLabStaffDashboardSummary(String currentDate, int currentUserId) {
        // --- 1. Xác định Khung thời gian Ngày hiện tại ---
        LocalDate searchDate = LocalDate.now();
        if (currentDate != null) {
            searchDate = LocalDate.parse(currentDate, DateTimeFormatter.ISO_LOCAL_DATE);
        }
        LocalDateTime startTime = searchDate.atStartOfDay(); // 00:00:00 hôm nay
        LocalDateTime endTime = searchDate.atTime(LocalTime.MAX); // 23:59:59.999999999 hôm nay

        // 2.Lấy thông tin LabStaff
        LabTechnician labTechnician = labTechnicianRepo.findByUserId(currentUserId);
        if (labTechnician == null) {
            throw new InvalidInputException("Nhân viên xét nghiệm không tồn tại.");
        }
        // --- 2. Truy vấn Dữ liệu ---

        // Tổng số lượng xét nghiệm trong ngày (Tất cả status)
        Integer totalPrescriptionsToday = labTestRepo.countByRequestedDateBetween(startTime, endTime);

        // Số lượng xét nghiệm chờ xử lý (Status = PAID, tất cả)
        Integer pendingForDispensing = labTestRepo.countByStatusAndRequestedDateBetween(
                ServiceStatus.PAID, startTime, endTime);

        // Số lượng xét nghiệm đang xử lý (Status = IN_PROGRESS, của tôi)
        Integer inProgressByMe = labTestRepo.countByStatusAndLabTechnicianAndRequestedDateBetween(
                ServiceStatus.IN_PROGRESS, labTechnician, startTime, endTime);

        // Số lượng xét nghiệm đã hoàn thành (Status = DISPENSED, của tôi)
        Integer completedByMe = labTestRepo.countByStatusAndLabTechnicianAndRequestedDateBetween(
                ServiceStatus.COMPLETED, labTechnician, startTime, endTime);

        // --- 3. Đóng gói và Trả về DTO ---
        return new DashboardOverview(
                totalPrescriptionsToday,
                pendingForDispensing,
                inProgressByMe,
                completedByMe);
    }

    // Lấy Tổng quan Dashboard cho Nhân viên nhà thuốc
    public DashboardOverview getPharmacistDashboardSummary(String currentDate, int currentUserId) {
        // --- 1. Xác định Khung thời gian Ngày hiện tại ---
        LocalDate searchDate = LocalDate.now();
        if (currentDate != null) {
            searchDate = LocalDate.parse(currentDate, DateTimeFormatter.ISO_LOCAL_DATE);
        }
        LocalDateTime startTime = searchDate.atStartOfDay(); // 00:00:00 hôm nay
        LocalDateTime endTime = searchDate.atTime(LocalTime.MAX); // 23:59:59.999999999 hôm nay

        // 2.Lấy thông tin PharmacyStaff
        PharmacyStaff pharmacyStaff = pharmacyStaffRepo.findByUserId(currentUserId);
        if (pharmacyStaff == null) {
            throw new InvalidInputException("Nhân viên nhà thuốc không tồn tại.");
        }
        // --- 2. Truy vấn Dữ liệu ---

        // Tổng số lượng đơn thuốc trong ngày (Tất cả status)
        Integer totalPrescriptionsToday = prescriptionRepo.countByPrescriptionDateBetween(startTime, endTime);

        // Số lượng Đơn thuốc chờ xử lý (Status = PAID, tất cả)
        Integer pendingForDispensing = prescriptionRepo.countByStatusAndPrescriptionDateBetween(
                PrescriptionStatus.PAID, startTime, endTime);

        // Số lượng đơn thuốc đang xử lý (Status = IN_PROGRESS, của tôi)
        Integer inProgressByMe = prescriptionRepo.countByStatusAndPharmacyStaffAndPrescriptionDateBetween(
                PrescriptionStatus.IN_PROGRESS, pharmacyStaff, startTime, endTime);

        // Số lượng đơn thuốc đã hoàn thành (Status = DISPENSED, của tôi)
        Integer completedByMe = prescriptionRepo.countByStatusAndPharmacyStaffAndPrescriptionDateBetween(
                PrescriptionStatus.COMPLETED, pharmacyStaff, startTime, endTime);

        // --- 3. Đóng gói và Trả về DTO ---
        return new DashboardOverview(
                totalPrescriptionsToday,
                pendingForDispensing,
                inProgressByMe,
                completedByMe);
    }

    // Lấy Tổng quan Bệnh nhân cho Bác sĩ
    public PatientOverview getPatientOverviewForDoctor(String currentDate, int currentUserId) {
        // --- 1. Xác định Khung thời gian Ngày hiện tại ---
        LocalDate searchDate = LocalDate.now();
        if (currentDate != null) {
            searchDate = LocalDate.parse(currentDate, DateTimeFormatter.ISO_LOCAL_DATE);
        }
        LocalDateTime startTime = searchDate.atStartOfDay(); // 00:00:00 hôm nay
        LocalDateTime endTime = searchDate.atTime(LocalTime.MAX); // 23:59:59.999999999 hôm nay

        // 2.Lấy thông tin Doctor
        Doctor doctor = doctorRepo.findByUserId(currentUserId);
        if (doctor == null) {
            throw new InvalidInputException("Bác sĩ không tồn tại.");
        }
        // --- 2. Truy vấn Dữ liệu ---
        // Tổng số lượng bệnh nhân trong ngày
        Integer totalPatientsToday = appointmentRepo.countByVisitDateTimeBetween(startTime, endTime);

        // Số lượng bệnh nhân trong ngày của bác sĩ
        Integer totalPatientsForMe = appointmentRepo.countPatientForDoctorToday(doctor.getId(), searchDate);

        // Số lượng cuộc hẹn khám trong ngày của bác sĩ
        Integer totalAppointmentsForMe = appointmentRepo
                .countConfirmedAndPendingAppointmentsForDoctorToday(doctor.getId(), searchDate);

        // Số lượng lượt khám đã hoàn thành
        Integer completedByMe = appointmentRepo.countCompletedAppointmentsForDoctorToday(doctor.getId(), searchDate);

        // --- 3. Đóng gói và Trả về DTO ---
        return new PatientOverview(
                totalPatientsToday,
                totalPatientsForMe,
                totalAppointmentsForMe,
                completedByMe);
    }

    public List<AppointmentUpcomingDTO> getUpcomingAppointmentForDoctor(int currentUserId) {
        // 2.Lấy thông tin Doctor
        Doctor doctor = doctorRepo.findByUserId(currentUserId);
        if (doctor == null) {
            throw new InvalidInputException("Bác sĩ không tồn tại.");
        }
        // Truy vấn cuộc hẹn sắp tới nhất
        List<AppointmentUpcomingDTO> upcomingAppointment = appointmentRepo
                .findAllUpcomingAppointmentsByDoctor(doctor.getId(), LocalDate.now())
                .stream()
                .map(appointment -> new AppointmentUpcomingDTO(
                        appointment.getId(),
                        appointment.getCode(),
                        appointment.getPatient().getUser().getFullname(),
                        appointment.getDoctorSchedule().getDate(),
                        appointment.getScheduleSlot().getStartTime() + " - "
                                + appointment.getScheduleSlot().getEndTime()))
                .collect(Collectors.toList());
        return upcomingAppointment;
    }

    // ==================== ADMIN DASHBOARD ====================

    /**
     * Lấy tổng quan Dashboard cho Admin
     * Bao gồm các thống kê:
     * - Tổng số bệnh nhân / Bệnh nhân mới hôm nay
     * - Lịch hẹn hôm nay (scheduled) / Lịch hẹn chờ xác nhận
     * - Doanh thu tháng
     * - Tổng nhân viên
     * - Thuốc sắp hết
     * - Bệnh nhân vãng lai hôm nay
     * - Bệnh nhân hoàn thành hôm nay
     * 
     * @param searchDateStr Ngày cần thống kê (định dạng yyyy-MM-dd), null = ngày
     *                      hiện tại
     * @return AdminDashboardOverviewDTO chứa các thống kê tổng quan
     */
    public AdminDashboardOverviewDTO getAdminDashboardOverview(String searchDateStr) {
        // --- 1. Xác định ngày thống kê ---
        LocalDate searchDate = LocalDate.now();
        if (searchDateStr != null && !searchDateStr.isEmpty()) {
            searchDate = LocalDate.parse(searchDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        }
        LocalDateTime startOfDay = searchDate.atStartOfDay();
        LocalDateTime endOfDay = searchDate.atTime(LocalTime.MAX);

        // --- 2. Truy vấn dữ liệu ---

        // 2.1. Tổng số bệnh nhân (tất cả)
        Long totalPatients = patientRepo.count();

        // 2.2. Bệnh nhân mới hôm nay (theo User.createdAt)
        Long newPatientsToday = patientRepo.countNewPatientsByDateRange(startOfDay, endOfDay);
        if (newPatientsToday == null)
            newPatientsToday = 0L;

        // 2.3. Lịch hẹn hôm nay (visitType = 'scheduled', theo doctorSchedule.date)
        Long appointmentsToday = appointmentRepo.countByVisitTypeAndDate("scheduled", searchDate);
        if (appointmentsToday == null)
            appointmentsToday = 0L;

        // 2.4. Lịch hẹn chờ xác nhận (status = 1, đặt hôm nay theo presentTime)
        Long pendingConfirmation = appointmentRepo.countByCurrentStatusAndPresentTimeRange(1, startOfDay, endOfDay);
        if (pendingConfirmation == null)
            pendingConfirmation = 0L;

        // 2.5. Doanh thu tháng (theo paymentDate trong tháng hiện tại)
        LocalDate firstDayOfMonth = searchDate.withDayOfMonth(1);
        LocalDate lastDayOfMonth = searchDate.with(TemporalAdjusters.lastDayOfMonth());
        BigDecimal monthlyRevenue = paymentRepo.sumRevenueByDateRange(
                firstDayOfMonth.atStartOfDay(),
                lastDayOfMonth.atTime(LocalTime.MAX));
        if (monthlyRevenue == null)
            monthlyRevenue = BigDecimal.ZERO;

        // 2.6. Tổng nhân viên
        Long totalStaff = staffRepo.count();

        // 2.7. Thuốc sắp hết (current_quantity <= minimum_quantity)
        Long lowStockMedicines = medicineRepo.countLowStockMedicines();
        if (lowStockMedicines == null)
            lowStockMedicines = 0L;

        // 2.8. Bệnh nhân vãng lai hôm nay (visitType = 'walk-in')
        Long walkInPatientsToday = appointmentRepo.countByVisitTypeAndDate("walk-in", searchDate);
        if (walkInPatientsToday == null)
            walkInPatientsToday = 0L;

        // 2.9. Bệnh nhân hoàn thành hôm nay (status = 5)
        Long completedPatientsToday = appointmentRepo.countByCurrentStatusAndDate(5, searchDate);
        if (completedPatientsToday == null)
            completedPatientsToday = 0L;

        // --- 3. Đóng gói và trả về DTO ---
        return new AdminDashboardOverviewDTO(
                totalPatients,
                newPatientsToday,
                appointmentsToday,
                pendingConfirmation,
                monthlyRevenue,
                totalStaff,
                lowStockMedicines,
                walkInPatientsToday,
                completedPatientsToday);
    }

    /**
     * Lấy thống kê theo tuần cho Admin
     * Trả về thông tin bệnh nhân và doanh thu theo từng ngày trong tuần
     * 
     * @param weekStartDateStr Ngày bắt đầu tuần (định dạng yyyy-MM-dd), null = tuần
     *                         hiện tại
     * @return WeeklyStatisticsDTO chứa thống kê theo tuần
     */
    public WeeklyStatisticsDTO getWeeklyStatistics(String weekStartDateStr) {
        // --- 1. Xác định tuần thống kê ---
        LocalDate weekStartDate;
        if (weekStartDateStr != null && !weekStartDateStr.isEmpty()) {
            weekStartDate = LocalDate.parse(weekStartDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        } else {
            // Lấy thứ Hai của tuần hiện tại
            weekStartDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        }
        LocalDate weekEndDate = weekStartDate.plusDays(6); // Chủ Nhật

        // --- 2. Truy vấn dữ liệu theo từng ngày ---
        List<WeeklyStatisticsDTO.DailyStatistic> dailyStatistics = new ArrayList<>();
        Long totalPatientsWeek = 0L;
        BigDecimal totalRevenueWeek = BigDecimal.ZERO;

        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = weekStartDate.plusDays(i);
            LocalDateTime startOfDay = currentDate.atStartOfDay();
            LocalDateTime endOfDay = currentDate.atTime(LocalTime.MAX);

            // Số bệnh nhân khám trong ngày
            Long patientCount = appointmentRepo.countByScheduleDate(currentDate);
            if (patientCount == null)
                patientCount = 0L;

            // Tổng doanh thu trong ngày
            BigDecimal dailyRevenue = paymentRepo.sumRevenueByDateRange(startOfDay, endOfDay);
            if (dailyRevenue == null)
                dailyRevenue = BigDecimal.ZERO;

            // Tên thứ tiếng Anh và tiếng Việt
            String dayOfWeek = currentDate.getDayOfWeek().name();
            String dayOfWeekVi = convertDayOfWeekToVietnamese(currentDate.getDayOfWeek());

            dailyStatistics.add(new WeeklyStatisticsDTO.DailyStatistic(
                    dayOfWeek,
                    dayOfWeekVi,
                    currentDate,
                    patientCount,
                    dailyRevenue));

            totalPatientsWeek += patientCount;
            totalRevenueWeek = totalRevenueWeek.add(dailyRevenue);
        }

        // --- 3. Đóng gói và trả về DTO ---
        return new WeeklyStatisticsDTO(
                weekStartDate,
                weekEndDate,
                dailyStatistics,
                totalPatientsWeek,
                totalRevenueWeek);
    }

    /**
     * Chuyển đổi DayOfWeek sang tiếng Việt
     */
    private String convertDayOfWeekToVietnamese(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> "Thứ Hai";
            case TUESDAY -> "Thứ Ba";
            case WEDNESDAY -> "Thứ Tư";
            case THURSDAY -> "Thứ Năm";
            case FRIDAY -> "Thứ Sáu";
            case SATURDAY -> "Thứ Bảy";
            case SUNDAY -> "Chủ Nhật";
        };
    }
}
