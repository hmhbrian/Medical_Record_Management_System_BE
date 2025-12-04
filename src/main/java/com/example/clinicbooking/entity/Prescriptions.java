package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "prescriptions")
public class Prescriptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String code;
    @Column(name = "total_days")
    private Integer TotalDays;
    @ManyToOne
    @JoinColumn(name = "record_id", nullable = true)
    private MedicalRecord record;
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    @ManyToOne
    @JoinColumn(name = "pharmacist_id", nullable = true)
    private PharmacyStaff pharmacyStaff;
    @Column(name = "prescription_date", nullable = false)
    private LocalDateTime prescriptionDate;
    @Column(name = "dispensed_at", nullable = true)
    private LocalDateTime dispensedAt;
    @Enumerated(EnumType.STRING) // Dùng Enum cho trạng thái để dễ quản lý
    @Column(nullable = false)
    private PrescriptionStatus status;
}
