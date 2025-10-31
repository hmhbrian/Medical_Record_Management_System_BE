package com.example.clinicbooking.DTO.Prescription;

import lombok.Data;

@Data
public class PrescriptionDetailsRequest {
    private Integer medicineId;
    private String dosage; // Liều dùng bằng chữ (VD: 1 viên x 2 lần)
    private Integer dailyQuantity; // Số lượng dùng trong một ngày
    private Integer quantity; // Số lượng cuối cùng
    private String notes;
    private Boolean isSubstitutable = false;
}
