package com.example.clinicbooking.DTO.Prescription.Detail;

import lombok.Data;

@Data
public class PrescriptionDetailsResponse {
    private Integer medicineId;
    private String medicineName;
    private String dosage; // Liều dùng bằng chữ (VD: 1 viên x 2 lần)
    private Integer dailyQuantity; // Số lượng dùng trong một ngày
    private Integer quantity; // Số lượng cuối cùng
    private String notes;
    private String unit;
    private Boolean isSubstitutable = false;
}
