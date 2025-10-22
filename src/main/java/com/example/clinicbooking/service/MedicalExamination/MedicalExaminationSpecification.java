package com.example.clinicbooking.service.MedicalExamination;

import com.example.clinicbooking.entity.Department;
import com.example.clinicbooking.entity.Doctor;
import com.example.clinicbooking.entity.MedicalRecord;
import com.example.clinicbooking.entity.Medical_Examination;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class MedicalExaminationSpecification {
    public static Specification<Medical_Examination> searchByKeyword(String keyword, Integer departmentId) {
       return (root, query, criteriaBuilder) -> {
           //Lọc theo departmentId
           Join<Medical_Examination, Department> departmentJoin = root.join("department", JoinType.INNER);
           Specification<Medical_Examination> spec = Specification.where(
                   (r, q, cb) -> cb.equal(departmentJoin.get("id"), departmentId)
           );

           if (keyword != null && !keyword.trim().isEmpty()) {
               String likeKeyword = "%" + keyword.toLowerCase().trim() + "%";

               Specification<Medical_Examination> searchSpec =  (rootSearch, querySearch, criteriaBuilderSearch) -> {
                   return criteriaBuilderSearch.or(
                           criteriaBuilderSearch.like(criteriaBuilderSearch.lower(root.get("examinationCode")), likeKeyword),
                           criteriaBuilderSearch.like(criteriaBuilderSearch.lower(root.get("examinationName")), likeKeyword)
                   );
               };
               spec = spec.and(searchSpec);
           }
              return spec.toPredicate(root, query, criteriaBuilder);
       };
    }
}
