package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "testtypes")
public class TestTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true, nullable = false)
    private String testCode;
    @Column(unique = true, nullable = false)
    private String testName;
    private double price;
    private String description;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    private int status;
}
