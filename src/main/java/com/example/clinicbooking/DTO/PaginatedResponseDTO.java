package com.example.clinicbooking.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponseDTO<T> {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private List<T> records;
}
