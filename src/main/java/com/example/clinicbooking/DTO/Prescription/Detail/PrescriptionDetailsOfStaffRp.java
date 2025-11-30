package com.example.clinicbooking.DTO.Prescription.Detail;

import com.example.clinicbooking.DTO.Patient.PatientSummary;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PrescriptionDetailsOfStaffRp {
    private Integer prescriptionId;
    private String prescriptionCode;
    private LocalDateTime prescriptionDate;
    private Integer totalDays; // Tổng ngày dùng
    private PatientSummary patient;
    private String doctorName;
    private String specialty;
    private List<PrescriptionDetailsResponse> details;
}
