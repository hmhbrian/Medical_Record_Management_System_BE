package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "medicalexamination")
public class Medical_Examination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "ExaminationCode", unique = true, nullable = false)
    private String examinationCode;
    @Column(name = "ExaminationName", unique = true, nullable = false)
    private String examinationName;
    private double price;
    private String description;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    private int status;
}
