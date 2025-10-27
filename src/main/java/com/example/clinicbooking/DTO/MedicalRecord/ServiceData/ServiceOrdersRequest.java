package com.example.clinicbooking.DTO.MedicalRecord.ServiceData;

import lombok.Data;

import java.util.List;

@Data
public class ServiceOrdersRequest {
    // ID của các loại xét nghiệm được chọn
    private List<Integer> labTestCatalogIds;
    // ID của các loại hình ảnh được chọn
    private List<Integer> imagingTypeIds;
}
