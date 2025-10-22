package com.example.clinicbooking.service.Icd10;

import com.example.clinicbooking.DTO.Icd10.Icd10Reponse;
import com.example.clinicbooking.DTO.MedicalExaminationResponse;
import com.example.clinicbooking.entity.Icd10;
import com.example.clinicbooking.entity.Medical_Examination;
import com.example.clinicbooking.repository.Icd10Repository;
import com.example.clinicbooking.service.MedicalExamination.MedicalExaminationSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Icd10Service {
    @Autowired
    private Icd10Repository icd10Repo;

    public List<Icd10Reponse> search(String keyword) {
        return icd10Repo.findAll(Icd10Specification.searchByKeyword(keyword))
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    private Icd10Reponse covertToResponse(Icd10 icd10) {
        Icd10Reponse dto = new Icd10Reponse();
        dto.setId(icd10.getId());
        dto.setCode(icd10.getCode());
        dto.setNameVn(icd10.getNameVn());
        dto.setNameEn(icd10.getNameEn());
        return dto;
    }
}

