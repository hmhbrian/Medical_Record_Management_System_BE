package com.example.ClinicBooking.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PrescriptionResponse {
    private int id;
    private int recordId;
    private int inpatientRecordId;
    private int doctorId;
    private String doctorName;
    private int pharmacistId;
    private String pharmacistName;
    private String status;
    private LocalDate prescriptionDate;
    private List<PrescriptionDetailResponse> details;
}
