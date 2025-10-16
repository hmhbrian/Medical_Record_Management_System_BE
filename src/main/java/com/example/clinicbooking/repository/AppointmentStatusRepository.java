package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppointmentStatusRepository extends JpaRepository<AppointmentStatus, Integer> {
    //Lấy trạng thái mới nhất của cuộc hẹn
    Optional<AppointmentStatus> findTopByAppointmentIdOrderByUpdateAtDesc(int id);
    //Lấy trạng thái chờ xác nhân của cuộc hẹn
    Optional<AppointmentStatus> findByAppointmentIdAndStatus(int id, int status);
    // Lấy toàn bộ lịch sử trạng thái (để hiển thị danh sách) và lấy luôn thông tin người cập nhật
    @EntityGraph(attributePaths = {"update_by"})
    List<AppointmentStatus> findByAppointmentIdOrderByUpdateAtDesc(int appointmentId);

}
