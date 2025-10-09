package com.example.clinicbooking.DTO.MedicalService;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

// Dùng cho cập nhật (partial update: chỉ field != null mới cập nhật)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateMedicalServiceRequest {
    private String code;
    private String name;
    private Double price;
    private String description;
    private Integer department_id; // đổi khoa nếu cần
    private Integer status;
}
