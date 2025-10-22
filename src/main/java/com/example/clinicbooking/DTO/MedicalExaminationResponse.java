package com.example.clinicbooking.DTO;

import com.example.clinicbooking.entity.Department;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class MedicalExaminationResponse {
    private int id;
    private String examinationCode;
    private String examinationName;
    private double price;
    private String description;
}
