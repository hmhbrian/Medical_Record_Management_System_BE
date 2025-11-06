package com.example.clinicbooking.DTO.LabTest;

import lombok.Data;

@Data
public class LabTestWaitingRequest {
    // Phân trang và Sắp xếp
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "requestedDate";
    private String sortDir = "DESC";

    private String keyword;
    private Integer specialtyId;
    private Integer doctorId;
    private String findDate;
}
