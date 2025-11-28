package com.example.clinicbooking.DTO.ImagingTest;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ImagingReportResponse {
    private Integer imagingTestId;
    private String imagingTestName;
    private LocalDateTime resultDate;
    private LocalDateTime requestedDate;
    private String doctorInChargeName;
    private String specialty;
    private String reportText;
    private String status;
    private String patientCode;
    private String patientName;
    // Danh sách các tệp hình ảnh liên quan
    private List<ImagingFileDTO> resultFiles;
}
