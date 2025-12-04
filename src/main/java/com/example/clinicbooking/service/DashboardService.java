package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.Dashboard.DashboardOverview;
import com.example.clinicbooking.DTO.Dashboard.Doctor.AppointmentUpcomingDTO;
import com.example.clinicbooking.DTO.Dashboard.Doctor.PatientOverview;
import com.example.clinicbooking.entity.*;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.*;
import com.example.clinicbooking.service.Prescription.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    // Lấy Tổng quan Dashboard cho Nhân viên xét nghiệm
    public DashboardOverview getLabStaffDashboardSummary(String currentDate, int currentUserId) {
        // --- 1. Xác định Khung thời gian Ngày hiện tại ---
        LocalDate searchDate = LocalDate.now();
        if(currentDate != null){
            searchDate = LocalDate.parse(currentDate, DateTimeFormatter.ISO_LOCAL_DATE);
        }
        LocalDateTime startTime = searchDate.atStartOfDay(); // 00:00:00 hôm nay
        LocalDateTime endTime = searchDate.atTime(LocalTime.MAX); // 23:59:59.999999999 hôm nay

        //2.Lấy thông tin LabStaff
        LabTechnician labTechnician = labTechnicianRepo.findByUserId(currentUserId);
        if(labTechnician == null){
            throw new InvalidInputException("Nhân viên xét nghiệm không tồn tại.");
        }
        // --- 2. Truy vấn Dữ liệu ---

        //Tổng số lượng xét nghiệm trong ngày (Tất cả status)
        Integer totalPrescriptionsToday = labTestRepo.countByRequestedDateBetween(startTime, endTime);

        //Số lượng xét nghiệm chờ xử lý (Status = PAID, tất cả)
        Integer pendingForDispensing = labTestRepo.countByStatusAndRequestedDateBetween(
                ServiceStatus.PAID, startTime, endTime);

        //Số lượng xét nghiệm đang xử lý (Status = IN_PROGRESS, của tôi)
        Integer inProgressByMe = labTestRepo.countByStatusAndLabTechnicianAndRequestedDateBetween(
                ServiceStatus.IN_PROGRESS, labTechnician, startTime, endTime);

        //Số lượng xét nghiệm đã hoàn thành (Status = DISPENSED, của tôi)
        Integer completedByMe = labTestRepo.countByStatusAndLabTechnicianAndRequestedDateBetween(
                ServiceStatus.COMPLETED, labTechnician, startTime, endTime);

        // --- 3. Đóng gói và Trả về DTO ---
        return new DashboardOverview(
                totalPrescriptionsToday,
                pendingForDispensing,
                inProgressByMe,
                completedByMe
        );
    }

    // Lấy Tổng quan Dashboard cho Nhân viên nhà thuốc
    public DashboardOverview getPharmacistDashboardSummary(String currentDate, int currentUserId) {
        // --- 1. Xác định Khung thời gian Ngày hiện tại ---
        LocalDate searchDate = LocalDate.now();
        if(currentDate != null){
            searchDate = LocalDate.parse(currentDate, DateTimeFormatter.ISO_LOCAL_DATE);
        }
        LocalDateTime startTime = searchDate.atStartOfDay(); // 00:00:00 hôm nay
        LocalDateTime endTime = searchDate.atTime(LocalTime.MAX); // 23:59:59.999999999 hôm nay

        //2.Lấy thông tin PharmacyStaff
        PharmacyStaff pharmacyStaff = pharmacyStaffRepo.findByUserId(currentUserId);
        if(pharmacyStaff == null){
            throw new InvalidInputException("Nhân viên nhà thuốc không tồn tại.");
        }
        // --- 2. Truy vấn Dữ liệu ---

        //Tổng số lượng đơn thuốc trong ngày (Tất cả status)
        Integer totalPrescriptionsToday = prescriptionRepo.countByPrescriptionDateBetween(startTime, endTime);

        //Số lượng Đơn thuốc chờ xử lý (Status = PAID, tất cả)
        Integer pendingForDispensing = prescriptionRepo.countByStatusAndPrescriptionDateBetween(
                PrescriptionStatus.PAID, startTime, endTime);

        //Số lượng đơn thuốc đang xử lý (Status = IN_PROGRESS, của tôi)
        Integer inProgressByMe = prescriptionRepo.countByStatusAndPharmacyStaffAndPrescriptionDateBetween(
                PrescriptionStatus.IN_PROGRESS, pharmacyStaff, startTime, endTime);

        //Số lượng đơn thuốc đã hoàn thành (Status = DISPENSED, của tôi)
        Integer completedByMe = prescriptionRepo.countByStatusAndPharmacyStaffAndPrescriptionDateBetween(
                PrescriptionStatus.COMPLETED, pharmacyStaff, startTime, endTime);

        // --- 3. Đóng gói và Trả về DTO ---
        return new DashboardOverview(
                totalPrescriptionsToday,
                pendingForDispensing,
                inProgressByMe,
                completedByMe
        );
    }

    // Lấy Tổng quan Bệnh nhân cho Bác sĩ
    public PatientOverview getPatientOverviewForDoctor(String currentDate, int currentUserId){
        // --- 1. Xác định Khung thời gian Ngày hiện tại ---
        LocalDate searchDate = LocalDate.now();
        if(currentDate != null){
            searchDate = LocalDate.parse(currentDate, DateTimeFormatter.ISO_LOCAL_DATE);
        }
        LocalDateTime startTime = searchDate.atStartOfDay(); // 00:00:00 hôm nay
        LocalDateTime endTime = searchDate.atTime(LocalTime.MAX); // 23:59:59.999999999 hôm nay

        //2.Lấy thông tin Doctor
        Doctor doctor = doctorRepo.findByUserId(currentUserId);
        if(doctor == null){
            throw new InvalidInputException("Bác sĩ không tồn tại.");
        }
        // --- 2. Truy vấn Dữ liệu ---
        //Tổng số lượng bệnh nhân trong ngày
        Integer totalPatientsToday = appointmentRepo.countByVisitDateTimeBetween(startTime, endTime);

        //Số lượng bệnh nhân trong ngày của bác sĩ
        Integer totalPatientsForMe = appointmentRepo.countPatientForDoctorToday(doctor.getId(), searchDate);

        //Số lượng cuộc hẹn khám trong ngày của bác sĩ
        Integer totalAppointmentsForMe = appointmentRepo.countConfirmedAndPendingAppointmentsForDoctorToday(doctor.getId(), searchDate);

        //Số lượng lượt khám đã hoàn thành
        Integer completedByMe = appointmentRepo.countCompletedAppointmentsForDoctorToday(doctor.getId(), searchDate);

        // --- 3. Đóng gói và Trả về DTO ---
        return new PatientOverview(
                totalPatientsToday,
                totalPatientsForMe,
                totalAppointmentsForMe,
                completedByMe
        );
    }

    public List<AppointmentUpcomingDTO> getUpcomingAppointmentForDoctor(int currentUserId) {
        //2.Lấy thông tin Doctor
        Doctor doctor = doctorRepo.findByUserId(currentUserId);
        if(doctor == null){
            throw new InvalidInputException("Bác sĩ không tồn tại.");
        }
        // Truy vấn cuộc hẹn sắp tới nhất
        List<AppointmentUpcomingDTO> upcomingAppointment = appointmentRepo.findAllUpcomingAppointmentsByDoctor(doctor.getId(), LocalDate.now())
                .stream()
                .map(appointment -> new AppointmentUpcomingDTO(
                        appointment.getId(),
                        appointment.getCode(),
                        appointment.getPatient().getUser().getFullname(),
                        appointment.getDoctorSchedule().getDate(),
                        appointment.getScheduleSlot().getStartTime() + " - " + appointment.getScheduleSlot().getEndTime()
                )).collect(Collectors.toList());
        return upcomingAppointment;
    }
}
