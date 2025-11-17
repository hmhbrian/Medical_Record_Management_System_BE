package com.example.clinicbooking.service.ImagingTest;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.ImagingTest.*;
import com.example.clinicbooking.DTO.LabTest.Detail.LabTestDetailResponse;
import com.example.clinicbooking.DTO.LabTest.Detail.ParameterDetailResponse;
import com.example.clinicbooking.DTO.LabTest.LabTestWaitingRequest;
import com.example.clinicbooking.DTO.LabTest.LabTestWaitingResponse;
import com.example.clinicbooking.DTO.PaginatedResponseDTO;
import com.example.clinicbooking.DTO.Patient.PatientSummary;
import com.example.clinicbooking.config.SupabaseStorageService;
import com.example.clinicbooking.entity.*;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.*;
import com.example.clinicbooking.service.LabTest.LabTestSpecification;
import com.example.clinicbooking.service.MedicalRecord.MedicalRecordService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ImagingTestService {
    private final ImagingTestsRepository imagingTestsRepo;
    private final ImagingResultFilesRepository imagingResultFilesRepo;
    private final ImagingStaffRepository imagingStaffRepo;
    private final MedicalRecordRepository medicalRecordRepo;
    private final StaffRepository staffRepo;
    private final SupabaseStorageService storageService;
    private final MedicalRecordService medicalRecordService;

    //=========POST METHODS=========
    //XÁC NHẬN ĐẢM NHẬN XÉT NGHIỆM
    public ApiResponse<?> assignImagingTest(Integer imagingTestId, Integer currentUserId) {
        //1. Lấy thông tin xét nghiệm
        ImagingTests imagingTests = imagingTestsRepo.findById(imagingTestId)
                .orElseThrow(() -> new InvalidInputException("Dịch vụ không tồn tại."));
        if (!imagingTests.getStatus().equals(ServiceStatus.PAID) || imagingTests.getImagingStaff() != null) {
            throw new InvalidInputException("Dịch vụ này không thể đảm nhận (đã có người làm hoặc đang tiến hành).");
        }

        //2.Lấy thông tin ImagingStaff
        ImagingStaff imagingStaff = imagingStaffRepo.findIdByUserId(currentUserId)
                .orElseThrow(() -> new InvalidInputException("Nhân viên không tồn tại."));


        //3. Gán nhân viên xét nghiệm
        imagingTests.setImagingStaff(imagingStaff);
        imagingTests.setStatus(ServiceStatus.IN_PROGRESS);
        imagingTests = imagingTestsRepo.save(imagingTests);

        //4. Cập nhật trạng thái hồ sơ ngoại trú thành "Đang chờ kết quả"
        MedicalRecord record = medicalRecordRepo.findById(imagingTests.getRecord().getId())
                .orElseThrow(() -> new InvalidInputException("Hồ sơ ngoại trú không tồn tại."));
        record.setStatus(MedicalRecordStatus.PENDING_RESULTS);
        medicalRecordRepo.save(record);

        return new ApiResponse<>(true, "Xác nhận thực hiện dịch vụ thành công", null);
    }

    public ApiResponse<?> uploadAndSaveImagingResults(
            Integer imagingTestId,
            ImagingResultUploadRequest request,
            Integer currentUserId) {

        // 0. Kiểm tra Input
        if (request.getFiles() == null || request.getFiles().isEmpty()) {
            throw new InvalidInputException("Vui lòng cung cấp ít nhất một tệp hình ảnh để tải lên.");
        }

        // Đảm bảo số lượng files và metadata khớp nhau
        int fileCount = request.getFiles().size();

        // 1. Xác thực Imaging Test
        ImagingTests imagingTest = imagingTestsRepo.findById(imagingTestId)
                .orElseThrow(() -> new InvalidInputException("Dịch vụ chẩn đoán hình ảnh không tồn tại."));

        // 2. Xác thực Nhân viên thực hiện
        ImagingStaff staff = imagingStaffRepo.findIdByUserId(currentUserId)
                .orElseThrow(() -> new InvalidInputException("Nhân viên chẩn đoán hình ảnh không tồn tại."));

        // Chỉ KTV được gán mới được phép tải ảnh lên.
        if (imagingTest.getImagingStaff() == null || imagingTest.getImagingStaff().getId() != staff.getId()) {
            throw new AccessDeniedException("Bạn không phải là người đảm nhận dịch vụ này để cập nhật chi tiết.");
        }

        // Chỉ cho phép tải ảnh khi trạng thái là IN_PROGRESS
        if (!imagingTest.getStatus().equals(ServiceStatus.IN_PROGRESS)) {
            throw new InvalidInputException("Dịch vụ đang ở trạng thái " + imagingTest.getStatus().name() + ". Không thể tải ảnh mới.");
        }

        // 3. Xử lý Tải File và Lưu vào Database
        for (int i = 0; i < fileCount; i++) {
            MultipartFile file = request.getFiles().get(i);
            String description = request.getDescriptions().get(i);

            //Filename: [RecordCode]_ImagingTest_[ImagingTestID]_[detailId].[extension]
            String fileExtension = getFileExtension(file.getOriginalFilename());
            String uniqueFileName = String.format("%s_ImagingTest_%d_%d.%s",
                    imagingTest.getRecord().getCode(),
                    imagingTest.getId(),
                    (i + 1),
                    fileExtension);
            // a. Tải tệp lên Cloud (Giả lập Supabase)
            String fileUrl = storageService.uploadFile(file, "imaging-results/", uniqueFileName);

            // b. Tìm bản ghi hiện tại dựa trên ImagingTestId và tên file (uniqueFileName)
            ImagingResultFiles resultFile = imagingResultFilesRepo.findByImagingTestsAndName(imagingTest, uniqueFileName)
                    .orElse(new ImagingResultFiles());

            resultFile.setImagingTests(imagingTest);
            resultFile.setFilePath(fileUrl);
            resultFile.setFileType(fileExtension); // Hàm lấy extension
            resultFile.setDescription(description);
            resultFile.setUpdatedAt(LocalDateTime.now());
            resultFile.setName(uniqueFileName);

            imagingResultFilesRepo.save(resultFile);
        }

        // 4. (Tùy chọn) Cập nhật kết quả, trạng thái ImagingTest
        if (request.getResult() != null) {
            imagingTest.setResult(request.getResult());
        }
        if (Boolean.TRUE.equals(request.getSendResult())) {
            // Gửi kết quả chính thức: Cập nhật trạng thái và ngày kết quả
            imagingTest.setStatus(ServiceStatus.COMPLETED);
            imagingTest.setResultDate(LocalDateTime.now());
            medicalRecordService.checkAndTransitionRecordStatus(imagingTest.getRecord());
        } else {
            if (!imagingTest.getStatus().equals(ServiceStatus.IN_PROGRESS) && !imagingTest.getStatus().equals(ServiceStatus.COMPLETED)) {
                imagingTest.setStatus(ServiceStatus.IN_PROGRESS);
            }
        }

        imagingTestsRepo.save(imagingTest);

        return new ApiResponse<>(true, "Tải lên " + fileCount + " hình ảnh kết quả thành công.", null);
    }

    // Hàm tiện ích đơn giản để lấy phần mở rộng của file
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    //=====GET METHODS=====//
    // HIỂN THỊ DỊCH VỤ CẦN THỰC HIỆN THEO BỘ LỌC VỚI PHÂN TRANG
    public PaginatedResponseDTO<ImagingTestWaitingResponse> searchImagingTestWaiting(ImagingTestWaitingRequest request) {

        // 1. Chuẩn bị phân trang và sắp xếp
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDir()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        // 2. Xây dựng Specification (logic lọc)
        Specification<ImagingTests> spec = ImagingTestSpecification.filterImagingTests(request,ServiceStatus.PAID.name(), null);

        // 3. Thực hiện truy vấn
        Page<ImagingTests> imagingTestsPage = imagingTestsRepo.findAll(spec, pageable);

        // 4. Ánh xạ (Mapping) Entity sang Response DTO
        List<ImagingTestWaitingResponse> responseImagingTest = imagingTestsPage.getContent().stream()
                .map(this::covertToWaitingResponse) // Sử dụng hàm covertToResponse để chuyển đổi
                .collect(Collectors.toList());

        // 5. Trả về Paginated Response
        return new PaginatedResponseDTO<ImagingTestWaitingResponse>(
                imagingTestsPage.getNumber(),
                imagingTestsPage.getSize(),
                imagingTestsPage.getTotalElements(),
                imagingTestsPage.getTotalPages(),
                responseImagingTest
        );
    }

    // Hàm chuyển đổi từ ImagingTest entity sang ImagingTestWaitingResponse DTO
    private ImagingTestWaitingResponse covertToWaitingResponse(ImagingTests imagingTests) {
        ImagingTestWaitingResponse dto = new ImagingTestWaitingResponse();
        dto.setImagingTestId(imagingTests.getId());
        dto.setImagingTestName(imagingTests.getImagingTypes().getImagingName());
        dto.setRequestedDate(imagingTests.getRequestedDate());
        dto.setDoctorInChargeName(imagingTests.getDoctor().getStaff().getUser().getFullname());
        dto.setStatus(imagingTests.getStatus().name());

        PatientSummary patientDto = new PatientSummary();
        patientDto.setPhoneNumber(imagingTests.getRecord().getPatient().getUser().getPhoneNumber());
        patientDto.setPatientCode(imagingTests.getRecord().getPatient().getPatientCode());
        patientDto.setFullName(imagingTests.getRecord().getPatient().getUser().getFullname());
        patientDto.setDateOfBirth(imagingTests.getRecord().getPatient().getUser().getDateOfBirth());
        dto.setPatient(patientDto);
        return dto;
    }

    // HIỂN THỊ XÉT NGHIỆM MÀ NHÂN VIÊN ĐẢM NHIỆM THEO BỘ LỌC VỚI PHÂN TRANG
    public PaginatedResponseDTO<ImagingTestOfStaffResponse> searchImagingTestOfStaff(ImagingTestWaitingRequest request, Integer currentUserId) {
        // Lấy thông tin kỹ thuật viên dựa trên userId
        ImagingStaff imagingStaff = imagingStaffRepo.findIdByUserId(currentUserId)
                .orElseThrow(() -> new InvalidInputException("Nhân viên chẩn đoán hình ảnh không tồn tại."));

        // 1. Chuẩn bị phân trang và sắp xếp
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDir()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        // 2. Xây dựng Specification (logic lọc)
        Specification<ImagingTests> spec = ImagingTestSpecification.filterImagingTests(request,ServiceStatus.IN_PROGRESS.name(), imagingStaff.getId());

        // 3. Thực hiện truy vấn
        Page<ImagingTests> imagingTestsPage = imagingTestsRepo.findAll(spec, pageable);

        // 4. Ánh xạ (Mapping) Entity sang Response DTO
        List<ImagingTestOfStaffResponse> responseImagingTest = imagingTestsPage.getContent().stream()
                .map(this::covertToOfStaffResponse) // Sử dụng hàm covertToResponse để chuyển đổi
                .collect(Collectors.toList());

        // 5. Trả về Paginated Response
        return new PaginatedResponseDTO<ImagingTestOfStaffResponse>(
                imagingTestsPage.getNumber(),
                imagingTestsPage.getSize(),
                imagingTestsPage.getTotalElements(),
                imagingTestsPage.getTotalPages(),
                responseImagingTest
        );
    }

    private ImagingTestOfStaffResponse covertToOfStaffResponse(ImagingTests imagingTests) {
        ImagingTestOfStaffResponse dto = new ImagingTestOfStaffResponse();
        dto.setImagingTestId(imagingTests.getId());
        dto.setImagingTestName(imagingTests.getImagingTypes().getImagingName());
        dto.setRequestedDate(imagingTests.getRequestedDate());
        dto.setDoctorInChargeName(imagingTests.getDoctor().getStaff().getUser().getFullname());
        dto.setStatus(imagingTests.getStatus().name());
        dto.setResult(imagingTests.getResult());
        dto.setResultDate(imagingTests.getResultDate());

        PatientSummary patientDto = new PatientSummary();
        patientDto.setPhoneNumber(imagingTests.getRecord().getPatient().getUser().getPhoneNumber());
        patientDto.setPatientCode(imagingTests.getRecord().getPatient().getPatientCode());
        patientDto.setFullName(imagingTests.getRecord().getPatient().getUser().getFullname());
        patientDto.setDateOfBirth(imagingTests.getRecord().getPatient().getUser().getDateOfBirth());
        dto.setPatient(patientDto);
        return dto;
    }

    //LẤY CHI TIẾT KẾT QUẢ XÉT NGHIỆM
    public ImagingReportResponse getLabTestDetails(Integer labTestId, Integer currentUserId) {

        // 1. Lấy thông tin Lab Test tổng quát
        ImagingTests imagingTest = imagingTestsRepo.findById(labTestId)
                .orElseThrow(() -> new InvalidInputException("Dịch vụ không tồn tại."));

        //Lấy thông tin nhân viên hiện tại để kiểm tra quyền truy cập
        Staff staff = staffRepo.findByUserId(currentUserId)
                .orElseThrow(() -> new InvalidInputException("Nhân viên không tồn tại."));

        //Chỉ NVXN Đảm nhận (LabTechnicianId) mới được xem chi tiết.
        if(staff.getStaff_position().getPosition().equals("Medical Imaging Technician")) {
            ImagingStaff imagingStaff = imagingStaffRepo.findIdByUserId(currentUserId)
                    .orElseThrow(() -> new InvalidInputException("Nhân viên không tồn tại."));

            if (imagingTest.getImagingStaff() == null || imagingTest.getImagingStaff().getId() != imagingStaff.getId()) {
                throw new AccessDeniedException("Bạn không được phép xem chi tiết dịch vụ này.");
            }
        }

        //Bác sĩ chỉ được xem kết quả khi đã có
        if(staff.getStaff_position().getPosition().equals("Doctor")) {
            // Đảm bảo kết quả đã có
            if (imagingTest.getResultDate() == null || imagingTest.getStatus() != ServiceStatus.COMPLETED) {
                throw new InvalidInputException("Kết quả chưa có.");
            }
        }

        // 2. Lấy chi tiết các chỉ số từ bảng imaging_result_files
        List<ImagingResultFiles> details = imagingResultFilesRepo.findAllByImagingTests(imagingTest);

        // 3. Ánh xạ sang Response DTO
        ImagingReportResponse response = new ImagingReportResponse();
        response.setImagingTestId(imagingTest.getId());
        response.setImagingName(imagingTest.getImagingTypes().getImagingName());
        response.setStatus(imagingTest.getStatus().name());
        response.setResultDate(imagingTest.getResultDate());
        response.setReportText(imagingTest.getResult()); // Ghi chú chung

        // Ánh xạ chi tiết các chỉ số
        List<ImagingFileDTO> imagingFiles = details.stream()
                .map(this::mapImagingTestDetailToDto)
                .collect(Collectors.toList());

        response.setResultFiles(imagingFiles);

        return response;
    }

    // Ánh xạ từ LabTestDetail entity sang ParameterDetailResponse DTO
    private ImagingFileDTO mapImagingTestDetailToDto(ImagingResultFiles resultFiles) {
        ImagingFileDTO dto = new ImagingFileDTO();
        dto.setId(resultFiles.getId());
        dto.setUrl(resultFiles.getFilePath());
        dto.setName(resultFiles.getName());
        dto.setDescription(resultFiles.getDescription());
        dto.setUpdatedAt(resultFiles.getUpdatedAt());
        return dto;
    }
}
