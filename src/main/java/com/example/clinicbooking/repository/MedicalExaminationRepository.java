package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Medical_Examination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalExaminationRepository extends JpaRepository<Medical_Examination, Integer>, JpaSpecificationExecutor<Medical_Examination> {
    boolean existsByExaminationName(String examinationName);
    long countByStatus(int status);

    @Query("select coalesce(sum(t.price), 0) from Medical_Examination  t")
    Double sumPrice();

    boolean existsByExaminationCodeAndIdNot(String examinationCode, int id);
    boolean existsByExaminationNameAndIdNot(String examinationName, int id);
}
