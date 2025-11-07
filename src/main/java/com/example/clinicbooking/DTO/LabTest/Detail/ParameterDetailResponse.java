package com.example.clinicbooking.DTO.LabTest.Detail;

import lombok.Data;

@Data
public class ParameterDetailResponse {
    private Integer detailId;
    private String parameterName;
    private String resultValue;
    private String unit;
    private String referenceRange; // Kết hợp min và max
    private Boolean isAbnormal;
    private String notes; // Ghi chú riêng cho chỉ số
}
