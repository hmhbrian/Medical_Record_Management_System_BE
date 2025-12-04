package com.example.clinicbooking.DTO.Order;

import lombok.Data;

@Data
public class OrderRequest {
    private String status;
    private Integer doctorId;
    private String keyword;
    private String findDate;

    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "requestedAt";
    private String sortDir = "ASC";
}
