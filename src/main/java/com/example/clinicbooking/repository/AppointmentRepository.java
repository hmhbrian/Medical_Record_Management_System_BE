package com.example.clinicbooking.repository;

import com.example.clinicbooking.DTO.PatientInScheduleResponse;
import com.example.clinicbooking.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer>,
        JpaSpecificationExecutor<Appointment> {
    List<Appointment> findByPatientId(int patientId);

    List<Appointment> findByDoctorScheduleId(int doctorScheduleId);

    List<Appointment> findByDoctorId(int doctorId);

    List<Appointment> findByDoctorIdAndDoctorSchedule_DateEqualsOrderByScheduleSlotAsc(Integer doctorId,
            LocalDate date);

    @Query("SELECT a FROM Appointment a " +
            "JOIN FETCH a.patient p " +
            "JOIN FETCH a.doctor d " +
            "JOIN FETCH d.staff s " +
            "JOIN FETCH s.user us " +
            "JOIN FETCH p.user up " +
            "WHERE a.id = :id")
    Optional<Appointment> findByIdWithDetails(@Param("id") int appointmentId);

    @Query("SELECT a FROM Appointment a WHERE a.doctorSchedule.date BETWEEN :startDate AND :endDate")
    List<Appointment> findByAppointmentDateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT a FROM Appointment a JOIN AppointmentStatus s ON a.id = s.appointment.id " +
            "WHERE s.status = :status " +
            "AND s.updateAt = (SELECT MAX(s2.updateAt) FROM AppointmentStatus s2 WHERE s2.appointment.id = a.id)")
    List<Appointment> findByStatus(@Param("status") int status);

    @Query("""
                select new com.example.clinicbooking.DTO.PatientInScheduleResponse(
                    p.id, p.user.fullname
                )
                from Appointment a
                join Patient p on a.patient.id = p.id
                    join AppointmentStatus s on a.id = s.appointment.id
                where a.doctorSchedule.id = :scheduleId AND s.status < 4
                order by p.user.fullname
            """)
    List<PatientInScheduleResponse> findPatientByDoctorScheduleId(@Param("scheduleId") int doctorScheduleId);

    // Truy vấn để đếm số lần khám cho một bác sĩ trong một ngày
    // @Query("SELECT COUNT(a) FROM Appointment a " +
    // "WHERE a.doctor.id = :doctorId AND FUNCTION('DATE',a.visitDateTime) =
    // :visitDate")
    @Query("SELECT MAX(a.visitNumber) FROM Appointment a " +
            "WHERE a.doctor.id = :doctorId AND a.doctorSchedule.id = :doctorScheduleId")
    Integer countVisitNumber(@Param("doctorId") Integer doctorId,
            @Param("doctorScheduleId") Integer doctorScheduleId);

    // Tổng số bệnh nhân khám trong ngày
    Integer countByVisitDateTimeBetween(LocalDateTime visitDateTimeAfter, LocalDateTime visitDateTimeBefore);

    // Tổng số cuộc hẹn được đặt trực tuyến diên ra trong ngày
    @Query("SELECT COUNT(a) FROM Appointment a JOIN AppointmentStatus ap on a.id = ap.appointment.id WHERE " +
            "a.doctor.id = :doctorId AND " +
            "a.doctorSchedule.date = :todayDate AND " +
            "a.visitType LIKE 'scheduled' AND " +
            "ap.appointment = a AND " +
            "ap.status IN (2, 3) AND " +
            "ap.updateAt = (SELECT MAX(as2.updateAt) FROM AppointmentStatus as2 WHERE as2.appointment = a)")
    Integer countConfirmedAndPendingAppointmentsForDoctorToday(
            @Param("doctorId") int doctorId,
            @Param("todayDate") LocalDate todayDate);

    // Tổng số lượt khám đã hoàn thành trong ngày
    @Query("SELECT COUNT(a) FROM Appointment a JOIN AppointmentStatus ap on a.id = ap.appointment.id  WHERE " +
            "a.doctor.id = :doctorId AND " +
            "a.doctorSchedule.date = :todayDate AND " +
            "ap.appointment = a AND " +
            "ap.status = 5 AND " +
            "ap.updateAt = (SELECT MAX(as2.updateAt) FROM AppointmentStatus as2 WHERE as2.appointment = a)")
    Integer countCompletedAppointmentsForDoctorToday(
            @Param("doctorId") int doctorId,
            @Param("todayDate") LocalDate todayDate);

    // Tổng số bệnh nhân trong ngày của bác sĩ
    @Query("SELECT COUNT(a) FROM Appointment a WHERE " +
            "a.doctor.id = :doctorId AND " +
            "a.doctorSchedule.date = :todayDate")
    Integer countPatientForDoctorToday(
            @Param("doctorId") int doctorId,
            @Param("todayDate") LocalDate todayDate);

    @Query("SELECT a FROM Appointment a JOIN AppointmentStatus ap on a.id = ap.appointment.id  WHERE " +
            "a.doctor.id = :doctorId AND " +
            "a.doctorSchedule.date >= :currentDate AND " +
            "ap.appointment = a AND " +
            "ap.status = 1 AND " +
            "ap.updateAt = (SELECT MAX(as2.updateAt) FROM AppointmentStatus as2 WHERE as2.appointment = a)"
            +
            "ORDER BY a.presentTime ASC")
    // Truy vấn cuộc hẹn sắp tới nhất
    List<Appointment> findAllUpcomingAppointmentsByDoctor(
            @Param("doctorId") int doctorId,
            @Param("currentDate") LocalDate currentDate);

    // ==================== APPOINTMENT OVERVIEW QUERIES ====================

    /**
     * Đếm số lượng lịch hẹn theo trạng thái hiện tại (tất cả thời gian)
     * Sử dụng subquery để lấy trạng thái mới nhất của mỗi cuộc hẹn
     * 
     * @param status Mã trạng thái (1-6)
     * @return Số lượng lịch hẹn có trạng thái hiện tại tương ứng
     */
    @Query("SELECT COUNT(a) FROM Appointment a " +
            "JOIN AppointmentStatus s ON a.id = s.appointment.id " +
            "WHERE s.status = :status " +
            "AND s.updateAt = (SELECT MAX(s2.updateAt) FROM AppointmentStatus s2 WHERE s2.appointment.id = a.id)")
    Long countByCurrentStatus(@Param("status") int status);

    /**
     * Đếm số lượng lịch hẹn theo trạng thái hiện tại và ngày cụ thể
     * 
     * @param status Mã trạng thái (1-6)
     * @param date   Ngày cần thống kê
     * @return Số lượng lịch hẹn thỏa mãn điều kiện
     */
    @Query("SELECT COUNT(a) FROM Appointment a " +
            "JOIN AppointmentStatus s ON a.id = s.appointment.id " +
            "WHERE s.status = :status " +
            "AND a.doctorSchedule.date = :date " +
            "AND s.updateAt = (SELECT MAX(s2.updateAt) FROM AppointmentStatus s2 WHERE s2.appointment.id = a.id)")
    Long countByCurrentStatusAndDate(@Param("status") int status, @Param("date") LocalDate date);

    /**
     * Đếm số người có mặt tại bệnh viện trong ngày (đã check-in)
     * Dựa vào trường presentTime để xác định người đã có mặt
     * 
     * @param date Ngày cần thống kê
     * @return Tổng số người có mặt
     */
    @Query("SELECT COUNT(a) FROM Appointment a " +
            "WHERE a.presentTime IS NOT NULL " +
            "AND a.doctorSchedule.date = :date")
    Long countPresentTodayByDate(@Param("date") LocalDate date);

    /**
     * Đếm số lượng lịch hẹn theo loại khách và ngày
     * 
     * @param visitType Loại khách ('scheduled' = hẹn trước, 'walk-in' = vãng lai)
     * @param date      Ngày cần thống kê
     * @return Số lượng lịch hẹn theo loại khách
     */
    @Query("SELECT COUNT(a) FROM Appointment a " +
            "WHERE a.visitType = :visitType " +
            "AND a.doctorSchedule.date = :date")
    Long countByVisitTypeAndDate(@Param("visitType") String visitType, @Param("date") LocalDate date);

    /**
     * Đếm số lịch hẹn chờ xác nhận được đặt trong khoảng thời gian
     * Dựa vào trường presentTime để xác định ngày đặt lịch
     * 
     * @param status        Trạng thái cần lọc (1 = chờ xác nhận)
     * @param startDateTime Thời điểm bắt đầu
     * @param endDateTime   Thời điểm kết thúc
     * @return Số lượng lịch hẹn chờ xác nhận
     */
    @Query("SELECT COUNT(a) FROM Appointment a " +
            "JOIN AppointmentStatus s ON a.id = s.appointment.id " +
            "WHERE s.status = :status " +
            "AND a.presentTime BETWEEN :startDateTime AND :endDateTime " +
            "AND s.updateAt = (SELECT MAX(s2.updateAt) FROM AppointmentStatus s2 WHERE s2.appointment.id = a.id)")
    Long countByCurrentStatusAndPresentTimeRange(
            @Param("status") int status,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);

    /**
     * Đếm tổng số bệnh nhân khám theo ngày
     * 
     * @param date Ngày cần thống kê
     * @return Số lượng bệnh nhân
     */
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.doctorSchedule.date = :date")
    Long countByScheduleDate(@Param("date") LocalDate date);
}
