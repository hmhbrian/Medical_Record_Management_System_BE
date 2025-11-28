package com.example.clinicbooking.service.Payment;

import com.example.clinicbooking.DTO.Payment.PaymentSearchRequest;
import com.example.clinicbooking.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PaymentSpecification {
    public static Specification<Payment> filterPayments(PaymentSearchRequest request) {
        return (root, query, criteriaBuilder) -> {

            // KHỞI TẠO spec MẶC ĐỊNH LÀ TRUE
            Specification<Payment> spec = Specification.where(null);

            // Lấy ngày hiện tại để làm mặc định nếu không có ngày tìm kiếm
            LocalDate defaultDate = LocalDate.now();

            //1. Xác định ngày tìm kiếm (Search Date)
            // Nếu request.getCreatedAt() có giá trị, dùng nó. Ngược lại, dùng ngày hiện tại.
            LocalDate searchLocalDate;
            if (request.getSearchDate() != null && !request.getSearchDate().isEmpty()) {
                try {
                    // Chuyển chuỗi ngày thành LocalDate
                    searchLocalDate = LocalDate.parse(request.getSearchDate(), DateTimeFormatter.ISO_LOCAL_DATE);
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

            // 3. Lọc theo Trạng thái
            if (request.getStatus() != null && !request.getStatus().isEmpty()) {
                spec = spec.and((r, q, cb) -> cb.equal(r.get("status"), request.getStatus()));
            }

            // 4. Tìm kiếm chung (Query)
            if (request.getQuery() != null && !request.getQuery().isEmpty()) {
                String likeQuery = "%" + request.getQuery().toLowerCase() + "%";

                // JOIN tới MedicalRecord để tìm theo Mã Hồ sơ
                Join<Payment, MedicalRecord> recordJoin = root.join("record", JoinType.INNER);
                // JOIN tới Patient và User để tìm theo Tên và Mã bệnh nhân
                Join<MedicalRecord, Patient> patientJoin = recordJoin.join("patient", JoinType.INNER);
                Join<Patient, User> userJoin = patientJoin.join("user", JoinType.INNER);

                Specification<Payment> searchSpec = (rootSearch, querySearch, criteriaBuilderSearch) -> {
                    return criteriaBuilderSearch.or(
                            // Tìm theo Mã Bệnh nhân
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(patientJoin.get("patientCode")), likeQuery),
                            // Tìm theo Tên Bệnh nhân
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(userJoin.get("fullname")), likeQuery),
                            // Tìm theo Mã Hồ sơ (code)
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(recordJoin.get("code")), likeQuery)
                    );
                };
                spec = spec.and(searchSpec);
            }

            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }

    // Hàm hỗ trợ tạo Specification để lọc trường LocalDateTime trong phạm vi một ngày.
    private static Specification<Payment> filterByDateRange(LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            // Tính thời điểm bắt đầu của ngày (ví dụ: 2025-11-01 00:00:00)
            LocalDateTime startOfDay = date.atStartOfDay();
            // Tính thời điểm kết thúc của ngày (ví dụ: 2025-11-01 23:59:59.999...)
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

            return criteriaBuilder.between(root.get("createdAt"), startOfDay, endOfDay);
        };
    }
}
