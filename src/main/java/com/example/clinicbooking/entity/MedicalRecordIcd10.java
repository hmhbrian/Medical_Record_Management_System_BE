package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "medical_record_icd10")
@Data
public class MedicalRecordIcd10 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "record_id", nullable = false)
    private MedicalRecord record;
    @ManyToOne
    @JoinColumn(name = "icd10_catalog_id", nullable = false)
    private Icd10 icd10;
    @Column(name = "is_principal", nullable = false)
    boolean isPrincipal;
    @Column(name = "diagnosis_order")
    int diagnosisOrder;

}
