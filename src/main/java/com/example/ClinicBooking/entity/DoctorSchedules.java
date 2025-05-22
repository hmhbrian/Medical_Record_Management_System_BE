package com.example.ClinicBooking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class DoctorSchedules {
    @Id
    @GeneratedValue
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
