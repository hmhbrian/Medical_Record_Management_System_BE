package com.example.clinicbooking.service.ImagingType;

import com.example.clinicbooking.entity.ImagingTypes;
import com.example.clinicbooking.entity.TestTypes;
import org.springframework.data.jpa.domain.Specification;

public class ImagingTypeSpecification {
    public static Specification<ImagingTypes> searchByKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            Specification<ImagingTypes> spec = Specification.where(null);

            if (keyword != null && !keyword.trim().isEmpty()) {
                String likeKeyword = "%" + keyword.toLowerCase().trim() + "%";

                Specification<ImagingTypes> searchSpec =  (rootSearch, querySearch, criteriaBuilderSearch) -> {
                    return criteriaBuilderSearch.or(
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(root.get("imagingCode")), likeKeyword),
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(root.get("imagingName")), likeKeyword)
                    );
                };
                spec = spec.and(searchSpec);
            }
            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }
}
