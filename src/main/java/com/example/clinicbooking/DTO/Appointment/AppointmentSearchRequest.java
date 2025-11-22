package com.example.clinicbooking.DTO.Appointment;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class AppointmentSearchRequest {
    private String keyword;          // tên bệnh nhân / mã lịch hẹn / tên bác sĩ
    private Integer status;           // trạng thái cần lọc theo trạng thái MỚI NHẤT
    private Integer departmentId;    // khoa (department) của specialty bác sĩ
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fromDate;      // chỉ lấy các lịch có date >= fromDate
    private Integer page = 0;        // mặc định trang 0
    private Integer size = 10;       // mặc định 10 bản ghi
    private String sortBy = "visitDateTime";
    private String sortDir = "DESC";
}
