package com.example.clinicbooking.DTO.MedicalRecord;

import lombok.Data;

@Data
public class MedicalRecordMetricsResponse {
    private int totalPatientsToday;            // Tổng số bệnh nhân có lịch khám/hồ sơ trong ngày
    private int pendingExamCount;              // Số hồ sơ chờ khám (Đã check-in, bác sĩ chưa mở)
    private int inProgressCount;               // Số hồ sơ đang khám/xử lý dở dang
    private int pendingResultCount;            // Số hồ sơ đang chờ kết quả xét nghiệm/hình ảnh
    private int pendingCompletionCount;        // Số hồ sơ đã có đủ thông tin, chờ ký đơn/hoàn tất
}
