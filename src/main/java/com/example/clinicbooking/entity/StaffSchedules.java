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
@Table(name = "staff_schedules")
public class StaffSchedules {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;
    @ManyToOne
    @JoinColumn(name = "shift_type_id")
    private Shift_type shiftType;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    private LocalDate date;
    private String status;
}
