package com.example.clinicbooking.service.Order;

import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordSearchAllRequest;
import com.example.clinicbooking.DTO.Order.OrderRequest;
import com.example.clinicbooking.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class OrderSpecification {
    public static Specification<Order> filterOrders(OrderRequest request) {
        return (root, query, criteriaBuilder) -> {

            // KHỞI TẠO spec MẶC ĐỊNH LÀ TRUE
            Specification<Order> spec = Specification.where(null);

            // 1. Join theo Doctor ID
            Join<Order, Doctor> doctorJoin = root.join("doctor", JoinType.INNER);

            // 2. Lọc bắt buộc theo doctor ID
            if(request.getDoctorId() != null) {
                spec = Specification.where(
                        (r, q, cb) -> cb.equal(doctorJoin.get("id"), request.getDoctorId())
                );
            }

            // 3. Lọc theo Trạng thái
            if (request.getStatus() != null && !request.getStatus().isEmpty()) {
                spec = spec.and((r, q, cb) -> cb.equal(r.get("status"), request.getStatus()));
            }


            LocalDate searchLocalDate;
            // 4. Lọc theo Ngày ra chỉ định
            if (request.getFindDate() != null && !request.getFindDate().isEmpty()){
                searchLocalDate = LocalDate.parse(request.getFindDate(), DateTimeFormatter.ISO_LOCAL_DATE);
                spec = spec.and(filterByDateRange(searchLocalDate));
            }

            // 5. Tìm kiếm chung (Query)
            if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
                String likeQuery = "%" + request.getKeyword().toLowerCase() + "%";

                // JOIN tới Patient và User để tìm theo Tên và Mã bệnh nhân
                Join<Order, MedicalRecord> recordJoin = root.join("medicalRecord", JoinType.INNER);

                Specification<Order> searchSpec = (rootSearch, querySearch, criteriaBuilderSearch) -> {
                    return criteriaBuilderSearch.or(
                            // Tìm theo Mã hồ sơ
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(recordJoin.get("code")), likeQuery),
                            // Tìm theo Tên dịch vụ
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(rootSearch.get("serviceName")), likeQuery)
                    );
                };
                spec = spec.and(searchSpec);
            }

            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }

    // Hàm hỗ trợ tạo Specification để lọc trường LocalDateTime trong phạm vi một ngày.
    private static Specification<Order> filterByDateRange(LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            // Tính thời điểm bắt đầu của ngày (ví dụ: 2025-11-01 00:00:00)
            LocalDateTime startOfDay = date.atStartOfDay();
            // Tính thời điểm kết thúc của ngày (ví dụ: 2025-11-01 23:59:59.999...)
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

            return criteriaBuilder.between(root.get("requestedAt"), startOfDay, endOfDay);
        };
    }

    //Trạng thái đang chờ (PAID, IN_PROGRESS)
    public static Specification<Order> isPending() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("status"), ServiceStatus.PAID),
                    criteriaBuilder.equal(root.get("status"), ServiceStatus.IN_PROGRESS)
            );
        };
    }

    //Y lệnh đã hoàn thành
    public static Specification<Order> isCompleted() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("status"), ServiceStatus.COMPLETED);
        };
    }

    // Hoàn thành Hôm nay
    public static Specification<Order> isCompletedToday() {
        return (root, query, criteriaBuilder) -> {
            // Lấy thời điểm bắt đầu và kết thúc của ngày hiện tại
            LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);

            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("status"), ServiceStatus.COMPLETED),
                    criteriaBuilder.between(root.get("completedAt"), startOfDay, endOfDay)
            );
        };
    }
}
