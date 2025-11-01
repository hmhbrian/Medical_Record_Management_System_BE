package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lab_test_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabTestDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "result_value")
    private String resultValue;
    @Column(name = "parameter_name")
    private String parameterName;
    @Column(name = "unit")
    private String unit;
    @Column(name = "min_reference_range")
    private String minReference;
    @Column(name = "max_reference_range")
    private String maxReference;
    private String notes;
    @Column(name = "is_abnormal")
    private Boolean isAbnormal;
    @ManyToOne
    @JoinColumn(name = "lab_test_id", nullable = false)
    private LabTests labTests;
    @ManyToOne
    @JoinColumn(name = "test_parameter_id", nullable = false)
    private LabParameter labParameter;
}
