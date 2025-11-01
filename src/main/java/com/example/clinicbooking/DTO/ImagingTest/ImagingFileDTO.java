package com.example.clinicbooking.DTO.ImagingTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImagingFileDTO {
    private String url;
    private String description;
    private String fileType;
}
