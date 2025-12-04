package com.example.clinicbooking.service.MedicalRecord;

import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordSearchAllRequest;
import com.example.clinicbooking.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AllMedicalRecordSpecification {
    public static Specification<MedicalRecord> filterRecords(MedicalRecordSearchAllRequest request) {
        return (root, query, criteriaBuilder) -> {

            // KHỞI TẠO spec MẶC ĐỊNH LÀ TRUE
            Specification<MedicalRecord> spec = Specification.where(null);

            // 1. Lọc bắt buộc theo Doctor ID
            Join<MedicalRecord, Doctor> doctorJoin = root.join("doctor", JoinType.INNER);
            if(request.getDoctorId() != null) {
                spec = Specification.where(
                        (r, q, cb) -> cb.equal(doctorJoin.get("id"), request.getDoctorId())
                );
            }

            // 2. Lọc bắt buộc theo Doctor ID
            Join<Doctor, Specialty> specialtyJoin = doctorJoin.join("specialty", JoinType.INNER);
            if(request.getDoctorId() != null) {
                spec = Specification.where(
                        (r, q, cb) -> cb.equal(specialtyJoin.get("id"), request.getSpecialtyId())
                );
            }

            // 3. Lọc theo Trạng thái
            if (request.getStatus() != null && !request.getStatus().isEmpty()) {
                spec = spec.and((r, q, cb) -> cb.equal(r.get("status"), request.getStatus()));
            }

//            //Lọc theo ngày khám
//            // Lấy ngày hiện tại để làm mặc định nếu không có ngày tìm kiếm
//            LocalDate defaultDate = LocalDate.now();
//
//            //Xác định ngày tìm kiếm (Search Date)
//            // Nếu request.getCurrentDate() có giá trị, dùng nó. Ngược lại, dùng ngày hiện tại.
//            LocalDate searchLocalDate;
//            if (request.getCurrentDate() != null && !request.getCurrentDate().isEmpty()) {
//                try {
//                    // Chuyển chuỗi ngày thành LocalDate
//                    searchLocalDate = LocalDate.parse(request.getCurrentDate(), DateTimeFormatter.ISO_LOCAL_DATE);
//                } catch (Exception e) {
//                    // Xử lý lỗi hoặc sử dụng ngày mặc định nếu định dạng sai
//                    searchLocalDate = defaultDate;
//                }
//            } else {
//                // Nếu không có ngày tìm kiếm, sử dụng ngày hiện tại (hoặc bỏ qua lọc nếu không muốn mặc định)
//                searchLocalDate = defaultDate;
//            }

            // 4. Lọc theo Ngày khám
            if( request.getCurrentDate() != null && !request.getCurrentDate().isEmpty()){
                LocalDate searchLocalDate = LocalDate.parse(request.getCurrentDate(), DateTimeFormatter.ISO_LOCAL_DATE);
                spec = spec.and(filterByDateRange(searchLocalDate));
            }

            // 5. Tìm kiếm chung (Query)
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
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(userJoin.get("fullname")), likeQuery),
                            // Tìm theo Mã Hồ sơ (ID)
                            criteriaBuilderSearch.like(rootSearch.get("code"), likeQuery)
                    );
                };
                spec = spec.and(searchSpec);
            }

            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }

    // Hàm hỗ trợ tạo Specification để lọc trường LocalDateTime trong phạm vi một ngày.
    private static Specification<MedicalRecord> filterByDateRange(LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            // Tính thời điểm bắt đầu của ngày (ví dụ: 2025-11-01 00:00:00)
            LocalDateTime startOfDay = date.atStartOfDay();
            // Tính thời điểm kết thúc của ngày (ví dụ: 2025-11-01 23:59:59.999...)
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

            // JOIN tới Appointment để tìm theo ngày khám
            Join<MedicalRecord, Appointment> appointmentJoin = root.join("appointment", JoinType.INNER);

            return criteriaBuilder.between(appointmentJoin.get("visitDateTime"), startOfDay, endOfDay);
        };
    }
}
