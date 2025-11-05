package com.example.clinicbooking.service.LabTest;

import com.example.clinicbooking.DTO.LabTest.LabTestDetailResponse;
import com.example.clinicbooking.DTO.LabTest.ParameterDetailResponse;
import com.example.clinicbooking.entity.LabTestDetail;
import com.example.clinicbooking.entity.LabTests;
import com.example.clinicbooking.entity.ServiceStatus;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.LabTestDetaiRepository;
import com.example.clinicbooking.repository.LabTestsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LabTestService {
    private final LabTestsRepository labTestRepo;
    private final LabTestDetaiRepository labTestDetailRepo;

    //LẤY CHI TIẾT KẾT QUẢ XÉT NGHIỆM
    public LabTestDetailResponse getLabTestDetails(Integer labTestId) {

        // 1. Lấy thông tin Lab Test tổng quát
        LabTests labTest = labTestRepo.findById(labTestId)
                .orElseThrow(() -> new InvalidInputException("Xét nghiệm không tồn tại."));

        // Đảm bảo kết quả đã có
        if (labTest.getResultDate() == null || labTest.getStatus() != ServiceStatus.COMPLETED) {
            throw new InvalidInputException("Kết quả xét nghiệm chưa có.");
        }

        // 2. Lấy chi tiết các chỉ số từ bảng lab_test_details
        List<LabTestDetail> details = labTestDetailRepo.findAllByLabTests(labTest);

        // 3. Ánh xạ sang Response DTO
        LabTestDetailResponse response = new LabTestDetailResponse();
        response.setLabTestId(labTest.getId());
        response.setTestName(labTest.getTestTypes().getTestName());
        response.setResultDate(labTest.getResultDate());
        response.setResult(labTest.getResult()); // Ghi chú chung

        // Ánh xạ chi tiết các chỉ số
        List<ParameterDetailResponse> parameterDtos = details.stream()
                .map(this::mapLabTestDetailToDto)
                .collect(Collectors.toList());

        response.setParameters(parameterDtos);

        return response;
    }

    private ParameterDetailResponse mapLabTestDetailToDto(LabTestDetail detail) {
        ParameterDetailResponse dto = new ParameterDetailResponse();
        dto.setParameterName(detail.getParameterName());
        dto.setResultValue(detail.getResultValue());
        dto.setUnit(detail.getUnit());

        // Kết hợp min và max thành chuỗi tham chiếu
        String reference = detail.getMinReference() + " - " + detail.getMaxReference();
        if(detail.getMinReference().equals(detail.getMaxReference())) {
            reference = detail.getMinReference();
        }
        dto.setReferenceRange(reference);
        dto.setIsAbnormal(detail.getIsAbnormal());
        dto.setNotes(detail.getNotes());
        return dto;
    }
}
