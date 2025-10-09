package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedule_slots")
public class ScheduleSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_schedule_id", nullable = false)
    private DoctorSchedules doctorSchedule;
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
    @Column(name = "is_booked", nullable = false)
    private Boolean isBooked;

    @PrePersist
    void prePersist() {
        if (isBooked == null) isBooked = false;
    }

    // Lưu ý về equals/hashCode:
    // chỉ nên dựa trên id để tránh lỗi khi entity chưa persist.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduleSlot that)) return false;
        return id != null && id.equals(that.id);
    }
    @Override
    public int hashCode() {
        return 31;
    }
}