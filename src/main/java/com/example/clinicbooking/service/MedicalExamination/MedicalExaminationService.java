package com.example.clinicbooking.service.MedicalExamination;

import com.example.clinicbooking.DTO.MedicalExaminationResponse;
import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordResponse;
import com.example.clinicbooking.DTO.PaginatedResponseDTO;
import com.example.clinicbooking.entity.MedicalRecord;
import com.example.clinicbooking.entity.Medical_Examination;
import com.example.clinicbooking.repository.DoctorRepository;
import com.example.clinicbooking.repository.MedicalExaminationRepository;
import com.example.clinicbooking.security.CustomUserDetails;
import com.example.clinicbooking.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicalExaminationService {
    @Autowired
    public MedicalExaminationRepository medicalExaminationRepo;
    @Autowired
    public DoctorRepository doctorRepo;

    public List<MedicalExaminationResponse> search(String keyword) {
        //0.Lấy id doctor từ user đang đăng nhập
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }
        Integer doctorId = doctorRepo.findIdByUserId(cud.getId());
        Integer departmentId = doctorRepo.findDepartmentIdByDoctorId(doctorId);


        return medicalExaminationRepo.findAll(MedicalExaminationSpecification.searchByKeyword(keyword, departmentId))
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    private MedicalExaminationResponse covertToResponse(Medical_Examination medicalExamination) {
        MedicalExaminationResponse dto = new MedicalExaminationResponse();
        dto.setId(medicalExamination.getId());
        dto.setExaminationName(medicalExamination.getExaminationName());
        dto.setExaminationCode(medicalExamination.getExaminationCode());
        dto.setPrice(medicalExamination.getPrice());
        dto.setDescription(medicalExamination.getDescription());
        return dto;
    }
}
