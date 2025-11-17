package com.example.clinicbooking.DTO.ImagingTest;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ImagingReportResponse {
    private Integer imagingTestId;
    private String imagingName;
    private LocalDateTime resultDate;
    private String reportText;
    private String status;
    // Danh sách các tệp hình ảnh liên quan
    private List<ImagingFileDTO> resultFiles;
}
