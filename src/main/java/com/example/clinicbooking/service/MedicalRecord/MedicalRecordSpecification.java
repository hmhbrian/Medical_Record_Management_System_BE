package com.example.clinicbooking.service.MedicalRecord;

import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordSearchRequest;
import com.example.clinicbooking.entity.Doctor;
import com.example.clinicbooking.entity.MedicalRecord;
import com.example.clinicbooking.entity.Patient;
import com.example.clinicbooking.entity.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class MedicalRecordSpecification {
    public static Specification<MedicalRecord> filterRecords(MedicalRecordSearchRequest request, int doctorId) {
        return (root, query, criteriaBuilder) -> {

            // 1. Lọc bắt buộc theo Doctor ID
            Join<MedicalRecord, Doctor> doctorJoin = root.join("doctor", JoinType.INNER);
            Specification<MedicalRecord> spec = Specification.where(
                    (r, q, cb) -> cb.equal(doctorJoin.get("id"), doctorId)
            );

            // 2. Lọc theo Trạng thái
            if (request.getStatus() != null && !request.getStatus().isEmpty()) {
                spec = spec.and((r, q, cb) -> cb.equal(r.get("status"), request.getStatus()));
            }

            // 3. Lọc theo Ngày khám
            if (request.getCurrentDate() != null) {
                // Giả sử request.getFromDate() đã được chuyển đổi sang LocalDate
                LocalDate current = LocalDate.parse(request.getCurrentDate());
                spec = spec.and((r, q, cb) -> cb.equal(r.get("visitDate"), current));

            }

            // 4. Tìm kiếm chung (Query)
            if (request.getQuery() != null && !request.getQuery().isEmpty()) {
                String likeQuery = "%" + request.getQuery().toLowerCase() + "%";

                // JOIN tới Patient và User để tìm theo Tên và Mã bệnh nhân
                Join<MedicalRecord, Patient> patientJoin = root.join("patient", JoinType.INNER);
                Join<Patient, User> userJoin = patientJoin.join("user", JoinType.INNER);

                Specification<MedicalRecord> searchSpec = (rootSearch, querySearch, criteriaBuilderSearch) -> {
                    return criteriaBuilderSearch.or(
                            // Tìm theo Mã Bệnh nhân
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(patientJoin.get("patientCode")), likeQuery),
                            // Tìm theo Tên Bệnh nhân
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(userJoin.get("fullName")), likeQuery),
                            // Tìm theo Mã Hồ sơ (ID)
                            criteriaBuilderSearch.like(rootSearch.get("code"), likeQuery)
                    );
                };
                spec = spec.and(searchSpec);
            }

            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }
}
