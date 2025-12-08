package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    @Query("""
                select p
                from Patient p
                where p.user.id = :userId
            """)
    Optional<Patient> findByUserId(Integer userId);

    Optional<Patient> findByPatientCodeContainingIgnoreCaseOrUser_PhoneNumberContainingIgnoreCaseOrUser_FullnameContainingIgnoreCase(
            String patientCode, String phoneNumber, String fullname);

    // ==================== ADMIN DASHBOARD QUERIES ====================

    /**
     * Đếm số bệnh nhân mới đăng ký theo ngày
     * Dựa vào trường User.createdAt để xác định ngày đăng ký
     * 
     * @param startDateTime Thời điểm bắt đầu ngày
     * @param endDateTime   Thời điểm kết thúc ngày
     * @return Số lượng bệnh nhân mới
     */
    @Query("SELECT COUNT(p) FROM Patient p WHERE p.user.createdAt BETWEEN :startDateTime AND :endDateTime")
    Long countNewPatientsByDateRange(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);
}
