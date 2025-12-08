package com.example.clinicbooking.DTO.Appointment;

import lombok.Data;

@Data
public class QueueSearchRequest {
    private String keyword; // tên bệnh nhân / mã lịch hẹn / tên bác sĩ
    private Integer status; // trạng thái cần lọc theo trạng thái MỚI NHẤT
    private Integer specialtyId; // chuyên khoa bác sĩ
    private String patientType;
    private String findDate; // chỉ lấy các lịch có date >= fromDate
    private Integer page = 0; // mặc định trang 0
    private Integer size = 10;
    private String sortBy = "visitDateTime";
    private String sortDir = "DESC";
}
