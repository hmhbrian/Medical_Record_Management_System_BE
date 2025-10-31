package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "lab_tests")
@Data
public class LabTests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "inpatient_record_id", nullable = true) // <-- Cần có nullable = true
    private InpatientRecord inpatientRecord;
    @ManyToOne
    @JoinColumn(name = "record_id", nullable = true)
    private MedicalRecord record;
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    @ManyToOne
    @JoinColumn(name = "lab_staff_id", nullable = true)
    private LabTechnician labTechnician;
    @ManyToOne
    @JoinColumn(name = "test_type_id", nullable = false)
    private TestTypes testTypes;
    @Column(name = "result", columnDefinition = "TEXT")
    private String result;
    @Column(name = "requested_date")
    private LocalDateTime requestedDate;
    @Column(name = "result_date")
    private LocalDateTime resultDate;
    @Enumerated(EnumType.STRING) // Dùng Enum cho trạng thái để dễ quản lý
    @Column(nullable = false)
    private ServiceStatus status;
}
