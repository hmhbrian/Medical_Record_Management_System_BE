package com.example.clinicbooking.DTO.Appointment;

import lombok.Data;

@Data
public class DoctorFilterByComplaintRequest {
    // Lý do khám mà NV tiếp nhận bệnh nhân nhập (ví dụ: "Đau dạ dày")
    private String chiefComplaint;
    // Ngày khám (để kiểm tra lịch làm việc của bác sĩ)
    private String date;
}
