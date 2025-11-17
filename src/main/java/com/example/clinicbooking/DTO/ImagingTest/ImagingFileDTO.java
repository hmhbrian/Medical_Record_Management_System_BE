package com.example.clinicbooking.DTO.ImagingTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImagingFileDTO {
    private Integer id;
    private String url;
    private String name;
    private String description;
    private LocalDateTime updatedAt;
}
