package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "icd10_catalog")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Icd10 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    @Column(name = "name_vn", nullable = false)
    private String nameVn;
    @Column(name = "name_en")
    private String nameEn;
    private String category;
}
