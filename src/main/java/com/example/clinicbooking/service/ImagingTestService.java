package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.ImagingTest.ImagingFileDTO;
import com.example.clinicbooking.DTO.ImagingTest.ImagingReportResponse;
import com.example.clinicbooking.entity.ImagingResultFiles;
import com.example.clinicbooking.entity.ImagingTests;
import com.example.clinicbooking.entity.ServiceStatus;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.ImagingResultFilesRepository;
import com.example.clinicbooking.repository.ImagingTestsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ImagingTestService {
    private final ImagingTestsRepository imagingTestsRepo;
    private final ImagingResultFilesRepository imagingResultFilesRepo;

    public ImagingReportResponse getImagingReport(Integer imagingTestId) {

        // 1. Lấy thông tin Imaging Test tổng quát (JOIN FETCH với bác sĩ CĐHA)
        ImagingTests imagingTest = imagingTestsRepo.findById(imagingTestId)
                .orElseThrow(() -> new InvalidInputException("Dịch vụ hình ảnh không tồn tại."));

        if (imagingTest.getResultDate() == null || imagingTest.getStatus() != ServiceStatus.COMPLETED) {
            throw new InvalidInputException("Báo cáo hình ảnh chưa được hoàn thành.");
        }

        // 2. Lấy danh sách các tệp hình ảnh liên quan
        List<ImagingResultFiles> files = imagingResultFilesRepo.findAllByImagingTests(imagingTest);

        // 3. Ánh xạ sang Response DTO
        ImagingReportResponse response = new ImagingReportResponse();
        response.setImagingTestId(imagingTest.getId());
        response.setImagingName(imagingTest.getImagingTypes().getImagingName());
        response.setResultDate(imagingTest.getResultDate());

        response.setReportText(imagingTest.getResult());

        // Ánh xạ danh sách tệp
        List<ImagingFileDTO> fileDtos = files.stream()
                .map(f -> new ImagingFileDTO(f.getFilePath(), f.getDescription(), f.getFileType()))
                .collect(Collectors.toList());

        response.setResultFiles(fileDtos);

        return response;
    }
}
