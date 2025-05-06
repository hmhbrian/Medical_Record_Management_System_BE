package com.example.ClinicBooking.repository;

import com.example.ClinicBooking.entity.Doctor;
import com.example.ClinicBooking.entity.DoctorSchedules;
import com.example.ClinicBooking.entity.Room;
import com.example.ClinicBooking.entity.Shift_type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DoctorSchedulesRepository extends JpaRepository<DoctorSchedules, Long> {
    boolean existsByDoctorAndDateAndShiftType(Doctor doctor, LocalDate date, Shift_type shiftType);
    boolean existsByRoomAndDateAndShiftType(Room room, LocalDate date, Shift_type shiftType);
    List<DoctorSchedules> findByDoctorIdOrderByDateAsc(int doctorId);
}
