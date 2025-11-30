package com.example.clinicbooking.DTO.Icd10;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Icd10Reponse {
    private Integer id;
    private String code;
    private String nameVn;
    private String nameEn;
    private String category;
}
