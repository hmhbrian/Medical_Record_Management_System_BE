package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Doctor;
import com.example.clinicbooking.entity.DoctorSchedules;
import com.example.clinicbooking.entity.Room;
import com.example.clinicbooking.entity.Shift_type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DoctorSchedulesRepository extends JpaRepository<DoctorSchedules, Integer> {
    boolean existsByDoctorAndDateAndShiftType(Doctor doctor, LocalDate date, Shift_type shiftType);
    boolean existsByRoomAndDateAndShiftType(Room room, LocalDate date, Shift_type shiftType);
    List<DoctorSchedules> findByDoctorIdAndDateAfterOrderByDateAsc(int doctorId, LocalDate currentDate);
    List<DoctorSchedules> findByDoctorIdAndDateBetweenOrderByShiftTypeIdAsc(int doctorId, LocalDate startDate, LocalDate endDate);
    boolean existsByIdAndDoctorId(int scheduleId, int doctorId);
}
