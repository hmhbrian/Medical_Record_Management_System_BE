package com.example.clinicbooking.DTO.MedicalService;

public enum EMedicalService {
    EXAMINATION("Khám bệnh"),
    LAB_TEST("Xét nghiệm"),
    IMAGING("Chẩn đoán hình ảnh");

    private final String description;

    EMedicalService(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
