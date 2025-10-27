package com.example.clinicbooking.service.TestType;

import com.example.clinicbooking.DTO.Services.MedicalExaminationResponse;
import com.example.clinicbooking.DTO.Services.TestTypeResponse;
import com.example.clinicbooking.entity.Medical_Examination;
import com.example.clinicbooking.entity.TestTypes;
import com.example.clinicbooking.repository.DoctorRepository;
import com.example.clinicbooking.repository.MedicalExaminationRepository;
import com.example.clinicbooking.repository.TestTypeRepository;
import com.example.clinicbooking.security.CustomUserDetails;
import com.example.clinicbooking.service.MedicalExamination.MedicalExaminationSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestTypeService {
    @Autowired
    public TestTypeRepository testTypeRepo;

    public List<TestTypeResponse> search(String keyword) {
        return testTypeRepo.findAll(TestTypeSpecification.searchByKeyword(keyword))
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    private TestTypeResponse covertToResponse(TestTypes testTypes) {
        TestTypeResponse dto = new TestTypeResponse();
        dto.setId(testTypes.getId());
        dto.setTestCode(testTypes.getTestCode());
        dto.setTestName(testTypes.getTestName());
        dto.setPrice(testTypes.getPrice());
        dto.setDescription(testTypes.getDescription());
        return dto;
    }
}
