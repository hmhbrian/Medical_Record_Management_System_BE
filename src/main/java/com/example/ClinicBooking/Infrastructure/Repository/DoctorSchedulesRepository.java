package com.example.ClinicBooking.Infrastructure.Repository;

import com.example.ClinicBooking.Domain.Entities.Doctor;
import com.example.ClinicBooking.Domain.Entities.DoctorSchedules;
import com.example.ClinicBooking.Domain.Entities.Room;
import com.example.ClinicBooking.Domain.Entities.Shift_type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DoctorSchedulesRepository extends JpaRepository<DoctorSchedules, Integer> {
    boolean existsByDoctorAndDateAndShiftType(Doctor doctor, LocalDate date, Shift_type shiftType);
    boolean existsByRoomAndDateAndShiftType(Room room, LocalDate date, Shift_type shiftType);
    List<DoctorSchedules> findByDoctorIdAndDateAfterOrderByDateAsc(int doctorId, LocalDate currentDate);
    List<DoctorSchedules> findByDoctorIdAndDateBetween(int doctorId, LocalDate startDate, LocalDate endDate);
}
