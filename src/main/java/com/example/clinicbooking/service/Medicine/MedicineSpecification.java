package com.example.clinicbooking.service.Medicine;

import com.example.clinicbooking.entity.Medicine;
import org.springframework.data.jpa.domain.Specification;

public class MedicineSpecification {
    public static Specification<Medicine> searchByKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            Specification<Medicine> spec = Specification.where(null);

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
}
