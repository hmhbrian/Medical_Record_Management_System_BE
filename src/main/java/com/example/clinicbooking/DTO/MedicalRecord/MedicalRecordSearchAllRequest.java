package com.example.clinicbooking.DTO.MedicalRecord;

import lombok.Data;

@Data
public class MedicalRecordSearchAllRequest {
    // Phân trang và Sắp xếp
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "id";
    private String sortDir = "DESC"; // ASC hoặc DESC

    // Lọc theo Thời gian
    private String currentDate; // Dạng YYYY-MM-DD

    // Lọc theo Trạng thái
    private String status;

    // Tìm kiếm chung (Tên, Mã BN, Mã HS)
    private String query;
    // Lọc theo Bác sĩ
    private Integer doctorId;
    // Lọc theo Chuyên khoa
    private Integer specialtyId;
}
