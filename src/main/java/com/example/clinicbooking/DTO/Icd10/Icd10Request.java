package com.example.clinicbooking.DTO.Icd10;

import lombok.Data;

@Data
public class Icd10Request {
    String code;
    String nameVn;
    String nameEn;
    String Category;
}
