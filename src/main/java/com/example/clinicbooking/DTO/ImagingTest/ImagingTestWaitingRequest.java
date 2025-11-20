package com.example.clinicbooking.DTO.ImagingTest;

import lombok.Data;

@Data
public class ImagingTestWaitingRequest {
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "requestedDate";
    private String sortDir = "DESC";

    private String keyword;
    private Integer specialtyId;
    private Integer doctorId;
    private String findDate;
}
