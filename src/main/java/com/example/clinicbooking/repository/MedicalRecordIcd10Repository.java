package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.MedicalRecordIcd10;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MedicalRecordIcd10Repository extends JpaRepository<MedicalRecordIcd10, Integer> {

    // Phương thức cần thiết để xóa tất cả các chẩn đoán ICD-10 cũ của hồ sơ
    void deleteByRecordId(Integer recordId);
    // Lấy tất cả mã ICD-10 đã liên kết với hồ sơ, sắp xếp theo thứ tự ưu tiên
    @Query("SELECT mri FROM MedicalRecordIcd10 mri JOIN FETCH mri.icd10 ic WHERE mri.record.id = :recordId ORDER BY mri.diagnosisOrder ASC")
    List<MedicalRecordIcd10> findByRecordIdOrderByDiagnosisOrder(Integer recordId);
}
