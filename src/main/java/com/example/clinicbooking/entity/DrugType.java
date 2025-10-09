package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "drugtype")
@Data
public class DrugType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
}
