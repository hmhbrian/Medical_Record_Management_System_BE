package com.example.clinicbooking.DTO.Payment;

import lombok.Data;

@Data
public class PaymentSearchRequest {
    // Phân trang và Sắp xếp
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "createdAt";
    private String sortDir = "ASC"; // ASC hoặc DESC

    // Lọc theo Thời gian
    private String searchDate; // Dạng YYYY-MM-DD

    // Lọc theo Trạng thái
    private String status;

    // Tìm kiếm chung (Tên, Mã BN, Mã HS)
    private String query;
}
