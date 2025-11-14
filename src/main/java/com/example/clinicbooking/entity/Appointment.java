package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String code;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "doctor_schedule_id")
    private DoctorSchedules doctorSchedule;

    private LocalDateTime presentTime;
    @OneToOne
    @JoinColumn(name = "schedule_slot_id", nullable = true)
    private ScheduleSlot scheduleSlot;
    @Column(name = "visit_date_time")
    private LocalDateTime visitDateTime;
    @Column(name = "visit_type")
    private String visitType;
    @Column(name = "visit_number")
    private Integer visitNumber;
}
