package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "imagingtypes")
public class ImagingTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "imaging_code", unique = true, nullable = false)
    private String imagingCode;
    @Column(name = "imaging_name", unique = true, nullable = false)
    private String imagingName;
    private double price;
    private String description;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    private int status; // 1: Active, 0: Inactive
}
