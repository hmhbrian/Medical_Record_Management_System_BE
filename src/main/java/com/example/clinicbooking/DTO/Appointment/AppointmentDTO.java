package com.example.clinicbooking.DTO.Appointment;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AppointmentDTO {
    private int id;
    private int doctorScheduleId;
    private int specialtyId;
    // Thông tin bệnh nhân
    private int patientId;
    private String code;
    private String patientName;
    private LocalDate patientYearOfBirth;
    private String patientGender;
    private String patientPhone;
    private String patientEmail;
    private int patientAge;
    // Thông tin bác sĩ
    private int doctorId;
    private String doctorName;
    private String doctorSpecialty;
    // Thông tin cuộc hẹn
    private String roomName;
    private String appointmentDate;
    private LocalDateTime presentTime;
    private String appointmentTime;
    private String appointmentType; //hẹn trực tiếp/ trực tuyến
    private String reason; //reason của trạng thái "Chờ xác nhận"
    private int statusId; // 1: Chờ xác nhận, 2: Đã xác nhận, 3:Hoàn thành, 4: Hủy.
    private String status;
    private BigDecimal totalPrice;
    private List<StatusHistoryItemDTO> statusHistory; // danh sách lịch sử trạng thái
}
