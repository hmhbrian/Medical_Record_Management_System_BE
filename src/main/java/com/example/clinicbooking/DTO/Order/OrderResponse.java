package com.example.clinicbooking.DTO.Order;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderResponse {
    //dịch vụ
    private Integer orderId;
    private String serviceName;
    private String serviceType;

    private String recordCode;
    //Doctor
    private String doctorName;
    private String doctorCode;
    private String specialty;
    //thông tin order
    private String result;
    private LocalDateTime requestedAt;
    private String timeExecution ;          //thời gian thực hiện dịch vụ (giờ)
    private LocalDateTime completedAt;
    private String status;
    //nhân viên thực hiện
    private String staffName;
    private String staffCode;
}
