package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lab_parameters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabParameter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String unit;
    private String min_reference;
    private String max_reference;
    @ManyToOne
    @JoinColumn(name = "test_type_id", nullable = false)
    private TestTypes testTypes;
}
