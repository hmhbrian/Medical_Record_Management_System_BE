package com.example.clinicbooking.service.Medicine;

import com.example.clinicbooking.entity.Medicine;
import org.springframework.data.jpa.domain.Specification;

import java.util.Calendar;
import java.util.Date;

public class MedicineSpecification {
    public static Specification<Medicine> searchByKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            //Kiếm tra hạn sử dụng
            Specification<Medicine> spec = isNotExpiredWithinTwoMonths();

            //Kiểm tra điều kiện tồn kho
            spec = spec.and(hasSufficientStock());

            if (keyword != null && !keyword.trim().isEmpty()) {
                String likeKeyword = "%" + keyword.toLowerCase().trim() + "%";

                Specification<Medicine> searchSpec =  (rootSearch, querySearch, criteriaBuilderSearch) -> {
                    return criteriaBuilderSearch.or(
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(root.get("medicineName")), likeKeyword),
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(root.get("active_ingredient")), likeKeyword)
                    );
                };
                spec = spec.and(searchSpec);
            }
            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }

    //Thêm điều kiện: hạn sử dụng (expirationDate) phải LỚN HƠN ngày hiện tại + 2 tháng.
    public static Specification<Medicine> isNotExpiredWithinTwoMonths() {
        return (root, query, criteriaBuilder) -> {
            // 1. Tính ngày mốc: Ngày hiện tại + 2 tháng
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 2);
            Date twoMonthsLater = calendar.getTime();

            // 2. Tạo Predicate: expirationDate > twoMonthsLater
            return criteriaBuilder.greaterThan(root.get("expirationDate"), twoMonthsLater);
        };
    }

    //Điều kiện tồn hàng: (current_quantity - reserved_quantity) > 20
    public static Specification<Medicine> hasSufficientStock() {
        return (root, query, criteriaBuilder) -> {
            // 1. Tạo biểu thức: current_quantity - reserved_quantity
            jakarta.persistence.criteria.Expression<Double> actualQuantity =
                    criteriaBuilder.diff(root.get("current_quantity"), root.get("reserved_quantity"));

            // 2. Tạo Predicate: (current_quantity - reserved_quantity) > 20
            // Dùng criteriaBuilder.greaterThan cho so sánh số học
            return criteriaBuilder.greaterThan(actualQuantity, 20.0);
        };
    }
}
