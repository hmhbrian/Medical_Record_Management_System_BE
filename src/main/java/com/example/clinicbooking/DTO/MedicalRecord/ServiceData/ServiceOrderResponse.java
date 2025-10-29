package com.example.clinicbooking.DTO.MedicalRecord.ServiceData;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ServiceOrderResponse {
    private Integer orderId; // ID trong kết quả chỉ định
    private String orderType; // Loại chỉ định: Xét nghiệm hay Hình ảnh
    private String Code;
    private String Name;
    private LocalDateTime requestDate;
    private String status;
    private String assignedStaffName;
}
