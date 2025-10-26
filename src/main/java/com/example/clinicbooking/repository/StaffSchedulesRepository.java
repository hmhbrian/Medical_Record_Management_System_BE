package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.StaffSchedules;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StaffSchedulesRepository extends JpaRepository<StaffSchedules, Integer> {
    //Kiểm tra xung đột ca làm việc cá nhân của nhân viên
    Integer countByStaffIdAndDateAndShiftTypeId(Integer staffId, LocalDate date, Integer shiftTypeId);
    //Kiểm tra xung đột ca làm việc theo phòng
    Integer countByRoomIdAndDateAndShiftTypeId(Integer roomId, LocalDate date, Integer shiftTypeId);
    //Lấy lịch làm việc của nhân viên trong khoảng thời gian
    List<StaffSchedules> findByStaffIdAndDateBetweenOrderByDateAsc(Integer staffId, LocalDate start, LocalDate end);
}
