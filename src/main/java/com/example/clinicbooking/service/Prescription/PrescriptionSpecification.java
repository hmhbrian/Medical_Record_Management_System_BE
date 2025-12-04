package com.example.clinicbooking.service.Prescription;

import com.example.clinicbooking.DTO.Prescription.PrescriptionSearchRequest;
import com.example.clinicbooking.DTO.Prescription.PrescriptionWaitingRequest;
import com.example.clinicbooking.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PrescriptionSpecification {
    public static Specification<Prescriptions> filterPrescriptions(PrescriptionWaitingRequest request, String status, Integer pharmacyStaffId) {
        return (root, query, criteriaBuilder) -> {
            // KHỞI TẠO spec MẶC ĐỊNH LÀ TRUE
            Specification<Prescriptions> spec = Specification.where(null);
            //Tìm theo status
            if(status != null) {
                spec = spec.and((r, q, cb) -> cb.equal(r.get("status"), status));
            }

            if(pharmacyStaffId != null){
                //lọc theo kỹ thuật viên
                Join<Prescriptions, PharmacyStaff> pharmacyStaffJoin = root.join("pharmacyStaff", JoinType.INNER);
                spec = spec.and((r, q, cb) -> cb.equal(pharmacyStaffJoin.get("id"), pharmacyStaffId));
            }

            // Lấy ngày hiện tại để làm mặc định nếu không có ngày tìm kiếm
            LocalDate defaultDate = LocalDate.now();

            //Xác định ngày tìm kiếm (Search Date)
            // Nếu request.getCreatedAt() có giá trị, dùng nó. Ngược lại, dùng ngày hiện tại.
            LocalDate searchLocalDate;
            if (request.getFindDate() != null && !request.getFindDate().isEmpty()) {
                try {
                    // Chuyển chuỗi ngày thành LocalDate
                    searchLocalDate = LocalDate.parse(request.getFindDate(), DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (Exception e) {
                    // Xử lý lỗi hoặc sử dụng ngày mặc định nếu định dạng sai
                    searchLocalDate = defaultDate;
                }
            } else {
                // Nếu không có ngày tìm kiếm, sử dụng ngày hiện tại (hoặc bỏ qua lọc nếu không muốn mặc định)
                searchLocalDate = defaultDate;
            }

            // 2. Lọc theo Ngày
            spec = spec.and(filterByDateRange(searchLocalDate));

            //3. lọc theo bác sĩ

            Join<Prescriptions, Doctor> doctorJoin = root.join("doctor", JoinType.INNER);
            if(request.getDoctorId() != null){
                spec = spec.and((r, q, cb) -> cb.equal(doctorJoin.get("id"), request.getDoctorId()));

            }

            // 4. Tìm kiếm chung (Query)
            if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
                String likeQuery = "%" + request.getKeyword().toLowerCase() + "%";

                // JOIN tới Patient và User để tìm theo Tên và Mã bệnh nhân
                Join<Prescriptions, MedicalRecord> recordJoin = root.join("record", JoinType.INNER);
                Join<MedicalRecord, Patient> patientJoin = recordJoin.join("patient", JoinType.INNER);
                Join<Patient, User> userJoin = patientJoin.join("user", JoinType.INNER);

                Specification<Prescriptions> searchSpec = (rootSearch, querySearch, criteriaBuilderSearch) -> {
                    return criteriaBuilderSearch.or(
                            // Tìm theo Mã Bệnh nhân
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(patientJoin.get("patientCode")), likeQuery),
                            // Tìm theo Tên Bệnh nhân
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(userJoin.get("fullName")), likeQuery)
                    );
                };
                spec = spec.and(searchSpec);
            }

            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }

    public static Specification<Prescriptions> filterAllPrescriptions(PrescriptionSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            // KHỞI TẠO spec MẶC ĐỊNH LÀ TRUE
            Specification<Prescriptions> spec = Specification.where(null);
            //Tìm theo status
            if(request.getStatus() != null) {
                spec = spec.and((r, q, cb) -> cb.equal(r.get("status"), request.getStatus()));
            }

            // Lấy ngày hiện tại để làm mặc định nếu không có ngày tìm kiếm
            LocalDate defaultDate = LocalDate.now();

            //Xác định ngày tìm kiếm (Search Date)
            // Nếu request.getCreatedAt() có giá trị, dùng nó. Ngược lại, dùng ngày hiện tại.

            // 2. Lọc theo Ngày
            if(request.getFindDate() != null && !request.getFindDate().isEmpty()) {
                LocalDate searchLocalDate = LocalDate.parse(request.getFindDate(), DateTimeFormatter.ISO_LOCAL_DATE);
                spec = spec.and(filterByDateRange(searchLocalDate));
            }

            //3. lọc theo bác sĩ

            Join<Prescriptions, Doctor> doctorJoin = root.join("doctor", JoinType.INNER);
            Join<Doctor, Specialty> specialtyJoin = doctorJoin.join("specialty", JoinType.INNER);
            if(request.getSpecialtyId() != null){
                spec = spec.and((r, q, cb) -> cb.equal(specialtyJoin.get("id"), request.getSpecialtyId()));

            }

            // 4. Tìm kiếm chung (Query)
            if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
                String likeQuery = "%" + request.getKeyword().toLowerCase() + "%";

                // JOIN tới Patient và User để tìm theo Tên và Mã bệnh nhân
                Join<Prescriptions, MedicalRecord> recordJoin = root.join("record", JoinType.INNER);
                Join<MedicalRecord, Patient> patientJoin = recordJoin.join("patient", JoinType.INNER);
                Join<Patient, User> patientUserJoin = patientJoin.join("user", JoinType.INNER);

                Join<Doctor, Staff> doctorStaffJoin = doctorJoin.join("staff", JoinType.INNER);
                Join<Staff, User> doctorUserJoin = doctorStaffJoin.join("user", JoinType.INNER);

                Specification<Prescriptions> searchSpec = (rootSearch, querySearch, criteriaBuilderSearch) -> {
                    return criteriaBuilderSearch.or(
                            // Tìm theo Mã Bệnh nhân
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(patientJoin.get("patientCode")), likeQuery),
                            // Tìm theo Tên Bệnh nhân
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(patientUserJoin.get("fullName")), likeQuery),
                            // Tìm theo Tên bác sĩ
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(doctorJoin.get("doctorcode")), likeQuery),
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(doctorUserJoin.get("fullname")), likeQuery)
                    );
                };
                spec = spec.and(searchSpec);
            }

            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }

    // Hàm hỗ trợ tạo Specification để lọc trường LocalDateTime trong phạm vi một ngày.
    private static Specification<Prescriptions> filterByDateRange(LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            // Tính thời điểm bắt đầu của ngày (ví dụ: 2025-11-01 00:00:00)
            LocalDateTime startOfDay = date.atStartOfDay();
            // Tính thời điểm kết thúc của ngày (ví dụ: 2025-11-01 23:59:59.999...)
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

            return criteriaBuilder.between(root.get("prescriptionDate"), startOfDay, endOfDay);
        };
    }
}
