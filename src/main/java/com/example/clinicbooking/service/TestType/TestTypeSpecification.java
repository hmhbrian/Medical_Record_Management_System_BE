package com.example.clinicbooking.service.TestType;

import com.example.clinicbooking.entity.TestTypes;
import org.springframework.data.jpa.domain.Specification;

public class TestTypeSpecification {
    public static Specification<TestTypes> searchByKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            Specification<TestTypes> spec = Specification.where(null);

            if (keyword != null && !keyword.trim().isEmpty()) {
                String likeKeyword = "%" + keyword.toLowerCase().trim() + "%";

                Specification<TestTypes> searchSpec =  (rootSearch, querySearch, criteriaBuilderSearch) -> {
                    return criteriaBuilderSearch.or(
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(root.get("testCode")), likeKeyword),
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(root.get("testName")), likeKeyword)
                    );
                };
                spec = spec.and(searchSpec);
            }
            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }
}
