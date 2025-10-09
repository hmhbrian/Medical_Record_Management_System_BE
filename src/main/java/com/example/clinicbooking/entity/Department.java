package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private String contact;
    private Date establishment_date;
    @Column(nullable = false)
    private int status = 0;//0: inactive, 1: active
    @OneToOne
    @JoinColumn(name = "head_doctor_id")
    private Doctor headDoctor;
}
