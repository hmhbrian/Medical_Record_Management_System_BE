package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSchedules {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "shift_type_id")
    private Shift_type shiftType;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    private LocalDate date;
    private String status;
    private int maxPatients;
    private int bookedPatients = 0;
}
