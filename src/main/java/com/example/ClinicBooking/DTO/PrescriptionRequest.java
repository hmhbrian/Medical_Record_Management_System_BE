package com.example.ClinicBooking.DTO;

import lombok.Data;

import java.util.List;

@Data
public class PrescriptionRequest {
    private Integer recordId;
    private Integer inpatientRecordId;
    private Integer doctorId;
    private Integer pharmacistId;
    private List<PrescriptionDetailRequest> details;
}
