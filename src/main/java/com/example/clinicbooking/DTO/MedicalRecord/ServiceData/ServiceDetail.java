package com.example.clinicbooking.DTO.MedicalRecord.ServiceData;

//Record để giữ thông tin dịch vụ tạm thời
public record ServiceDetail(String serviceType, Integer serviceId, String description, double price) {
}
