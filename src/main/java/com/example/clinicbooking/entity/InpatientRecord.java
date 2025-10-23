package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "inpatient_records")
@Data
public class InpatientRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "record_id", nullable = false)
    private MedicalRecord record;
    @ManyToOne
    @JoinColumn(name = "bed_id", nullable = false)
    private Bed bed;
    private LocalDateTime admissionDate;
    private LocalDateTime dischargeDate;
    private String treatmentPlan;
    private String status;
}
