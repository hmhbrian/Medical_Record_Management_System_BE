package com.example.clinicbooking.service.Icd10;

import com.example.clinicbooking.entity.Department;
import com.example.clinicbooking.entity.Icd10;
import com.example.clinicbooking.entity.Medical_Examination;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class Icd10Specification {
    public static Specification<Icd10> searchByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return Specification.where(null); // Trả về Specification rỗng nếu không có keyword
        }

        String likeKeyword = "%" + keyword.toLowerCase().trim() + "%";

        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("code")),likeKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("nameVn")),likeKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("nameEn")), likeKeyword)
            );
        };
    }
}
