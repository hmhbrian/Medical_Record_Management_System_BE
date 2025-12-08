package com.example.clinicbooking.DTO.MedicalRecord.ServiceData;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ServiceOrderResponse {
    private Integer orderId; // ID trong kết quả chỉ định
    private String orderType; // Loại chỉ định: Xét nghiệm hay Hình ảnh
    private String Code;
    private String Name;
    private LocalDateTime requestDate;
    private String status;
    private LocalDateTime resultDate;
    private String result;
    private String assignedStaffName;
    private String assignedStaffCode;
}
