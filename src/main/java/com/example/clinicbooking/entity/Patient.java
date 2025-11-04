package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "patient_code", unique = true, nullable = false)
    private String patientCode;
    @Column(name = "medical_history")
    private String medicalHistory;
    @Column(name = "insurance_number")
    private String insuranceNumber;
    @Column(name = "insurance_rate")
    private Double insuranceRate;
}
