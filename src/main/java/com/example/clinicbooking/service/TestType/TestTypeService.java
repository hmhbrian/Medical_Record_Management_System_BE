package com.example.clinicbooking.service.TestType;

import com.example.clinicbooking.DTO.LabParameter.LabParameterDTO;
import com.example.clinicbooking.DTO.LabParameter.TestTypeParameterDetailDTO;
import com.example.clinicbooking.DTO.Services.MedicalExaminationResponse;
import com.example.clinicbooking.DTO.Services.TestTypeResponse;
import com.example.clinicbooking.entity.LabParameter;
import com.example.clinicbooking.entity.Medical_Examination;
import com.example.clinicbooking.entity.TestTypes;
import com.example.clinicbooking.exceptions.ResourceNotFoundException;
import com.example.clinicbooking.repository.DoctorRepository;
import com.example.clinicbooking.repository.LabParameterRepository;
import com.example.clinicbooking.repository.MedicalExaminationRepository;
import com.example.clinicbooking.repository.TestTypeRepository;
import com.example.clinicbooking.security.CustomUserDetails;
import com.example.clinicbooking.service.MedicalExamination.MedicalExaminationSpecification;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TestTypeService {
    private final TestTypeRepository testTypeRepo;
    private final LabParameterRepository labParameterRepo;

    //====GET METHODS====//
    //TÌM KIẾM LOẠI XÉT NGHIỆM TRÊN DS THEO TỪ KHÓA
    public List<TestTypeResponse> search(String keyword) {
        return testTypeRepo.findAll(TestTypeSpecification.searchByKeyword(keyword))
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    //LẤY DANH SÁCH THAM SỐ THEO LOẠI XÉT NGHIỆM
    public TestTypeParameterDetailDTO getTestParameters(Integer testTypeId) {

        // 1. Tìm TestType. Nếu không tìm thấy, ném ngoại lệ 404
        TestTypes testType = testTypeRepo.findById(testTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy loại xét nghiệm với ID: " + testTypeId));

        // 2. Lấy danh sách các tham số của loại xét nghiệm đó
        List<LabParameter> parameters = labParameterRepo.findByTestTypes(testType);

        // 3. Chuyển đổi (Mapping) từ Entity sang DTO
        List<LabParameterDTO> parameterDTOs = parameters.stream()
                .map(this::convertToParameterDTO)
                .collect(Collectors.toList());

        // 4. Tạo và trả về DTO phản hồi chính
        TestTypeParameterDetailDTO resultDTO = new TestTypeParameterDetailDTO();
        resultDTO.setTestTypeId(testType.getId());
        resultDTO.setTestName(testType.getTestName());
        resultDTO.setParameters(parameterDTOs);

        return resultDTO;
    }

    //====PUT METHODS====//
    @Transactional // Đảm bảo tất cả thao tác (thêm, sửa, xóa) là nguyên tử
    public void saveAllParameters(Integer testTypeId, List<LabParameterDTO> newParameterDTOs) {

        // 1. Kiểm tra TestType tồn tại
        TestTypes testType = testTypeRepo.findById(testTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Test type not found with ID: " + testTypeId));

        // Lấy danh sách ID tham số cũ
        List<LabParameter> existingParameters = labParameterRepo.findByTestTypes(testType);
        Set<Integer> existingIds = existingParameters.stream()
                .map(LabParameter::getId)
                .collect(Collectors.toSet());

        // Lấy danh sách ID tham số mới (không null)
        Set<Integer> newIds = newParameterDTOs.stream()
                .filter(dto -> dto.getParameterId() != null)
                .map(LabParameterDTO::getParameterId)
                .collect(Collectors.toSet());

        // ------------------
        // A. Xử lý XÓA (DELETE)
        // ------------------

        List<Integer> idsToDelete = existingIds.stream()
                .filter(id -> !newIds.contains(id)) // ID nào cũ mà không có trong danh sách mới
                .collect(Collectors.toList());

        if (!idsToDelete.isEmpty()) {
            labParameterRepo.deleteAllById(idsToDelete);
        }

        // ------------------
        // B. Xử lý THÊM & SỬA (INSERT & UPDATE)
        // ------------------
        for (LabParameterDTO dto : newParameterDTOs) {
            LabParameter parameter;

            if (dto.getParameterId() == null || !existingIds.contains(dto.getParameterId())) {
                // THÊM MỚI (INSERT): ID là null hoặc không có trong CSDL (dùng DTO mới hoàn toàn)
                parameter = new LabParameter();
            } else {
                // SỬA (UPDATE): ID tồn tại trong CSDL
                parameter = existingParameters.stream()
                        .filter(p -> p.getId().equals(dto.getParameterId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Parameter ID not found for update logic."));
            }

            // Cập nhật các trường từ DTO vào Entity
            parameter.setName(dto.getName());
            parameter.setUnit(dto.getUnit());
            parameter.setMin_reference(dto.getMinReference());
            parameter.setMax_reference(dto.getMaxReference());
            parameter.setTestTypes(testType);

            labParameterRepo.save(parameter);
        }
    }

    //====CONVERSION METHODS====//
    //HÀM CHUYỂN ĐỔI TỪ ENTITY SANG DTO RESPONSE
    private TestTypeResponse covertToResponse(TestTypes testTypes) {
        TestTypeResponse dto = new TestTypeResponse();
        dto.setId(testTypes.getId());
        dto.setTestCode(testTypes.getTestCode());
        dto.setTestName(testTypes.getTestName());
        dto.setPrice(testTypes.getPrice());
        dto.setDescription(testTypes.getDescription());
        return dto;
    }

    //HÀM CHUYỂN ĐỔI TỪ ENTITY SANG DTO PARAMETER
    private LabParameterDTO convertToParameterDTO(LabParameter parameter) {
        return new LabParameterDTO(
                parameter.getId(),
                parameter.getName(),
                parameter.getUnit(),
                parameter.getMin_reference(),
                parameter.getMax_reference()
        );
    }
}
