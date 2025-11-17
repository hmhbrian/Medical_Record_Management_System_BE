package com.example.clinicbooking.DTO.Appointment;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class QueueResponse {
    private int appointmentId;
    // Thông tin bệnh nhân
    private String code;
    private String patientName;
    private LocalDate patientYearOfBirth;
    private String patientGender;
    private int patientAge;
    // Thông tin bác sĩ
    private String doctorName;
    private String doctorSpecialty;
    // Thông tin cuộc hẹn
    private String roomName;
    private String shift;
    private String patientType; //Loại bệnh nhân (schedule/walk-in)
    private LocalDateTime visitDateTime;//thời gian check-in
    private String appointmentTime; // khung giờ cuộc hẹn(nếu là schedule)
    private int visitNumber; //Số thứ tự khám bệnh
    private String reason; //reason của trạng thái "Chờ xác nhận"
    private int statusId; // : Chờ xác nhận, 2: Đã xác nhận, 3:Chờ khám, 4:Đang khám, 5:Hoàn thành, 6: Hủy.
    private String status;
}
