package com.example.ClinicBooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;
    private String doctorcode;
    @ManyToOne
    @JoinColumn(name = "specialty_id")
    private Specialty specialty;

    private int experienceYears;
    private String certificationName;
    private String issuedBy;
    private LocalDate issueDate;
}
