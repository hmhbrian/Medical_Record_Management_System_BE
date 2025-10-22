package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "medical_records")
@Data
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
    @Column(name = "visit_date")
    private LocalDate visitDate;
    @Column(name = "visit_number")
    private Integer visitNumber;
    @Column(name = "initial_symptoms")
    private String initialSymptoms;
    private String diagnosis;
    @Enumerated(EnumType.STRING) // Dùng Enum cho trạng thái để dễ quản lý
    @Column(nullable = false)
    private MedicalRecordStatus status;
    private String notes;
}
