package com.example.clinicbooking.service.LabTest;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.LabTest.*;
import com.example.clinicbooking.DTO.LabTest.Detail.LabTestDetailRequest;
import com.example.clinicbooking.DTO.LabTest.Detail.LabTestDetailResponse;
import com.example.clinicbooking.DTO.LabTest.Detail.ParameterDetailRequest;
import com.example.clinicbooking.DTO.LabTest.Detail.ParameterDetailResponse;
import com.example.clinicbooking.DTO.PaginatedResponseDTO;
import com.example.clinicbooking.DTO.Patient.PatientSummary;
import com.example.clinicbooking.entity.*;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.*;
import com.example.clinicbooking.service.MedicalRecord.MedicalRecordService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class LabTestService {
    private final LabTestsRepository labTestRepo;
    private final LabTestDetaiRepository labTestDetailRepo;
    private final LabParameterRepository labParameterRepo;
    private final LabTechnicianRepository labTechnicianRepo;
    private final MedicalRecordRepository medicalRecordRepo;
    private final TestTypeRepository testTypeRepo;
    private final StaffRepository staffRepo;
    private final MedicalRecordService medicalRecordService;

    //=========POST METHODS=========
    //XÁC NHẬN ĐẢM NHẬN XÉT NGHIỆM
    public ApiResponse<?> assignLabTest(Integer labTestId, Integer currentUserId) {
        //1. Lấy thông tin xét nghiệm
        LabTests labTest = labTestRepo.findById(labTestId)
                .orElseThrow(() -> new InvalidInputException("Xét nghiệm không tồn tại."));
        if (!labTest.getStatus().equals(ServiceStatus.PAID) || labTest.getLabTechnician() != null) {
            throw new InvalidInputException("Xét nghiệm này không thể đảm nhận (đã có người làm hoặc đang tiến hành).");
        }

        //2.Lấy thông tin LabStaff
        Integer labTechnicianId = labTechnicianRepo.findIdByUserId(currentUserId);
        LabTechnician labTechnician = labTechnicianRepo.findById(labTechnicianId)
                .orElseThrow(() -> new InvalidInputException("Nhân viên xét nghiệm không tồn tại."));


        //3. Gán nhân viên xét nghiệm
        labTest.setLabTechnician(labTechnician);
        labTest.setStatus(ServiceStatus.IN_PROGRESS);
        labTest = labTestRepo.save(labTest);

        //4. Tạo trước chi tiết xét nghiệm
        this.createInitialLabTestDetails(labTest);

        //5. Cập nhật trạng thái hồ sơ ngoại trú thành "Đang chờ kết quả"
        MedicalRecord record = medicalRecordRepo.findById(labTest.getRecord().getId())
                .orElseThrow(() -> new InvalidInputException("Hồ sơ ngoại trú không tồn tại."));
        record.setStatus(MedicalRecordStatus.PENDING_RESULTS);
        medicalRecordRepo.save(record);

        return new ApiResponse<>(true, "Xác nhận thực hiện xét nghiệm thành công", null);
    }

    //Tạo các bản ghi LabTestDetail dựa trên LabParameters
    private void createInitialLabTestDetails(LabTests labTest) {
        // Lấy TestTypeId
        Integer testTypeId = labTest.getTestTypes().getId();
        TestTypes testType = testTypeRepo.findById(testTypeId)
                .orElseThrow(() -> new InvalidInputException("Loại xét nghiệm không tồn tại."));

        // 1. Lấy tất cả tham số chuẩn liên quan
        List<LabParameter> parameters = labParameterRepo.findByTestTypes(testType);

        //2. Khai báo danh sách để lưu chi tiết xét nghiệm
        List<LabTestDetail> detailsToSave = new ArrayList<>();

        //3. Lặp và tạo LabTestDetail
        for (LabParameter param : parameters) {
            LabTestDetail detail = new LabTestDetail();
            detail.setLabTests(labTest); // Thiết lập mối quan hệ
            detail.setLabParameter(param);

            // Sao chép các thông số cố định
            detail.setParameterName(param.getName());
            detail.setUnit(param.getUnit());
            detail.setMinReference(param.getMin_reference());
            detail.setMaxReference(param.getMax_reference());

            // Khởi tạo các trường kết quả/ghi chú rỗng/mặc định
            detail.setResultValue("");
            detail.setIsAbnormal(false);
            detail.setNotes("");

            detailsToSave.add(detail);
        }

        // 4. Lưu toàn bộ chi tiết xét nghiệm vào CSDL
        labTestDetailRepo.saveAll(detailsToSave);
    }

    //CẬP NHẬT KẾT QUẢ XÉT NGHIỆM
    public void updateLabTestResults(Integer labTestId, LabTestDetailRequest updateDTO, Integer currentUserId) {

        // 1. Lấy LabTest chính và xác thực
        LabTests labTest = labTestRepo.findById(labTestId)
                .orElseThrow(() -> new InvalidInputException("Xét nghiệm không tồn tại."));

        //Chỉ NVXN Đảm nhận (LabTechnicianId) mới được cập nhật.
        Integer labTechnicianId = labTechnicianRepo.findIdByUserId(currentUserId);
        LabTechnician labTechnician = labTechnicianRepo.findById(labTechnicianId)
                .orElseThrow(() -> new InvalidInputException("Nhân viên xét nghiệm không tồn tại."));

        if (labTest.getLabTechnician() == null || labTest.getLabTechnician().getId() != labTechnician.getId()) {
            throw new AccessDeniedException("Bạn không được phép cập nhật chi tiết xét nghiệm này.");
        }

        // Nếu là Lưu tạm (finalizeResult=false), trạng thái vẫn là IN_PROGRESS và resultDate không đổi.
        labTestRepo.save(labTest);

        // 2. Cập nhật LabTest Details (Danh sách chi tiết)
        // Lấy tất cả detailId từ DTO để tìm kiếm hàng loạt (tối ưu hơn việc tìm từng dòng)
        List<Integer> detailIds = updateDTO.getParameters().stream()
                .map(ParameterDetailRequest::getDetailId)
                .collect(Collectors.toList());

        // Lấy tất cả Entity cần cập nhật trong 1 query
        List<LabTestDetail> existingDetails = labTestDetailRepo.findAllById(detailIds);

        // Chuyển danh sách DTO thành Map để dễ dàng tìm kiếm khi cập nhật
        Map<Integer, ParameterDetailRequest> updateMap = updateDTO.getParameters().stream()
                .collect(Collectors.toMap(ParameterDetailRequest::getDetailId, Function.identity()));

        List<LabTestDetail> updatedDetails = existingDetails.stream()
                .filter(d -> updateMap.containsKey(d.getId()))
                .peek(d -> {
                    ParameterDetailRequest updateData = updateMap.get(d.getId());
                    // Cập nhật các trường
                    d.setResultValue(updateData.getResultValue());
                    d.setNotes(updateData.getNotes());
                    d.setIsAbnormal(updateData.getIsAbnormal());
                })
                .collect(Collectors.toList());

        labTestDetailRepo.saveAll(updatedDetails); // Lưu tất cả các thay đổi chi tiết

        // 3. Cập nhật LabTest (thông tin tổng hợp)
        labTest.setResult(updateDTO.getResult());
        if (updateDTO.getFinalizeResult()) {
            // Hoàn tất: Cập nhật ngày kết quả và trạng thái thành COMPLETED
            labTest.setResultDate(LocalDateTime.now());
            labTest.setStatus(ServiceStatus.COMPLETED);
            medicalRecordService.checkAndTransitionRecordStatus(labTest.getRecord());
        }
    }

    //=========GET METHODS=========
    //LẤY CHI TIẾT KẾT QUẢ XÉT NGHIỆM
    public LabTestDetailResponse getLabTestDetails(Integer labTestId, Integer currentUserId) {

        // 1. Lấy thông tin Lab Test tổng quát
        LabTests labTest = labTestRepo.findById(labTestId)
                .orElseThrow(() -> new InvalidInputException("Xét nghiệm không tồn tại."));

        //Lấy thông tin nhân viên hiện tại để kiểm tra quyền truy cập
        Staff staff = staffRepo.findByUserId(currentUserId)
                .orElseThrow(() -> new InvalidInputException("Nhân viên không tồn tại."));

        //Chỉ NVXN Đảm nhận (LabTechnicianId) mới được xem chi tiết.
        if(staff.getStaff_position().getPosition().equals("Lab Technician")) {
            Integer labTechnicianId = labTechnicianRepo.findIdByUserId(currentUserId);
            LabTechnician labTechnician = labTechnicianRepo.findById(labTechnicianId)
                    .orElseThrow(() -> new InvalidInputException("Nhân viên xét nghiệm không tồn tại."));

            if (labTest.getLabTechnician() == null || labTest.getLabTechnician().getId() != labTechnician.getId()) {
                throw new AccessDeniedException("Bạn không được phép xem chi tiết xét nghiệm này.");
            }
        }

        //Bác sĩ chỉ được xem kết quả khi đã có
        if(staff.getStaff_position().getPosition().equals("Doctor")) {
            // Đảm bảo kết quả đã có
            if (labTest.getResultDate() == null || labTest.getStatus() != ServiceStatus.COMPLETED) {
                throw new InvalidInputException("Kết quả xét nghiệm chưa có.");
            }
        }

        // 2. Lấy chi tiết các chỉ số từ bảng lab_test_details
        List<LabTestDetail> details = labTestDetailRepo.findAllByLabTests(labTest);

        // 3. Ánh xạ sang Response DTO
        LabTestDetailResponse response = new LabTestDetailResponse();
        response.setLabTestId(labTest.getId());
        response.setTestName(labTest.getTestTypes().getTestName());
        response.setStatus(labTest.getStatus().name());
        response.setResultDate(labTest.getResultDate());
        response.setResult(labTest.getResult()); // Ghi chú chung
        response.setRequestedDate(labTest.getRequestedDate());
        response.setPatientName(labTest.getRecord().getPatient().getUser().getFullname());
        response.setDoctorInChargeName(labTest.getDoctor().getStaff().getUser().getFullname());

        // Ánh xạ chi tiết các chỉ số
        List<ParameterDetailResponse> parameterDtos = details.stream()
                .map(this::mapLabTestDetailToDto)
                .collect(Collectors.toList());

        response.setParameters(parameterDtos);

        return response;
    }

    // Ánh xạ từ LabTestDetail entity sang ParameterDetailResponse DTO
    private ParameterDetailResponse mapLabTestDetailToDto(LabTestDetail detail) {
        ParameterDetailResponse dto = new ParameterDetailResponse();
        dto.setDetailId(detail.getId());
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

    // HIỂN THỊ XÉT NGHIỆM CẦN THỰC HIỆN THEO BỘ LỌC VỚI PHÂN TRANG
    public PaginatedResponseDTO<LabTestWaitingResponse> searchLabTestWaiting(LabTestWaitingRequest request) {

        // 1. Chuẩn bị phân trang và sắp xếp
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDir()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        // 2. Xây dựng Specification (logic lọc)
        Specification<LabTests> spec = LabTestSpecification.filterLabTests(request,ServiceStatus.PAID.name(), null);

        // 3. Thực hiện truy vấn
        Page<LabTests> labTestsPage = labTestRepo.findAll(spec, pageable);

        // 4. Ánh xạ (Mapping) Entity sang Response DTO
        List<LabTestWaitingResponse> responseLabTest = labTestsPage.getContent().stream()
                .map(this::covertToWaitingResponse) // Sử dụng hàm covertToResponse để chuyển đổi
                .collect(Collectors.toList());

        // 5. Trả về Paginated Response
        return new PaginatedResponseDTO<LabTestWaitingResponse>(
                labTestsPage.getNumber(),
                labTestsPage.getSize(),
                labTestsPage.getTotalElements(),
                labTestsPage.getTotalPages(),
                responseLabTest
        );
    }

    // Hàm chuyển đổi từ LabTests entity sang LabTestWaitingResponse DTO
    private LabTestWaitingResponse covertToWaitingResponse(LabTests labTests) {
        LabTestWaitingResponse dto = new LabTestWaitingResponse();
        dto.setLabTestId(labTests.getId());
        dto.setLabTestName(labTests.getTestTypes().getTestName());
        dto.setRequestedDate(labTests.getRequestedDate());
        dto.setDoctorInChargeName(labTests.getDoctor().getStaff().getUser().getFullname());
        dto.setSpecialty(labTests.getDoctor().getSpecialty().getName());
        dto.setStatus(labTests.getStatus().name());

        PatientSummary patientDto = new PatientSummary();
        patientDto.setPhoneNumber(labTests.getRecord().getPatient().getUser().getPhoneNumber());
        patientDto.setPatientCode(labTests.getRecord().getPatient().getPatientCode());
        patientDto.setFullName(labTests.getRecord().getPatient().getUser().getFullname());
        patientDto.setDateOfBirth(labTests.getRecord().getPatient().getUser().getDateOfBirth());
        dto.setPatient(patientDto);
        return dto;
    }

    // HIỂN THỊ XÉT NGHIỆM MÀ NHÂN VIÊN ĐẢM NHIỆM THEO BỘ LỌC VỚI PHÂN TRANG
    public PaginatedResponseDTO<LabTestOfStaffResponse> searchTestOfLabStaff(LabTestWaitingRequest request, Integer currentUserId) {
        //0.Lấy thông tin LabStaff
        Integer labTechnicianId = labTechnicianRepo.findIdByUserId(currentUserId);
        LabTechnician labTechnician = labTechnicianRepo.findById(labTechnicianId)
                .orElseThrow(() -> new InvalidInputException("Nhân viên xét nghiệm không tồn tại."));

        // 1. Chuẩn bị phân trang và sắp xếp
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDir()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        // 2. Xây dựng Specification (logic lọc)
        Specification<LabTests> spec = LabTestSpecification.filterLabTests(request,null, labTechnician.getId());

        // 3. Thực hiện truy vấn
        Page<LabTests> labTestsPage = labTestRepo.findAll(spec, pageable);

        // 4. Ánh xạ (Mapping) Entity sang Response DTO
        List<LabTestOfStaffResponse> responseLabTest = labTestsPage.getContent().stream()
                .map(this::covertToTestOfStaffResponse) // Sử dụng hàm covertToResponse để chuyển đổi
                .collect(Collectors.toList());

        // 5. Trả về Paginated Response
        return new PaginatedResponseDTO<LabTestOfStaffResponse>(
                labTestsPage.getNumber(),
                labTestsPage.getSize(),
                labTestsPage.getTotalElements(),
                labTestsPage.getTotalPages(),
                responseLabTest
        );
    }

    // Hàm chuyển đổi từ LabTests entity sang LabTestOfStaffResponse DTO
    private LabTestOfStaffResponse covertToTestOfStaffResponse(LabTests labTests) {
        LabTestOfStaffResponse dto = new LabTestOfStaffResponse();
        dto.setLabTestId(labTests.getId());
        dto.setLabTestName(labTests.getTestTypes().getTestName());
        dto.setRequestedDate(labTests.getRequestedDate());
        dto.setDoctorInChargeName(labTests.getDoctor().getStaff().getUser().getFullname());
        dto.setSpecialty(labTests.getDoctor().getSpecialty().getName());
        dto.setStatus(labTests.getStatus().name());
        dto.setResult(labTests.getResult());
        dto.setResultDate(labTests.getResultDate());

        PatientSummary patientDto = new PatientSummary();
        patientDto.setPhoneNumber(labTests.getRecord().getPatient().getUser().getPhoneNumber());
        patientDto.setPatientCode(labTests.getRecord().getPatient().getPatientCode());
        patientDto.setFullName(labTests.getRecord().getPatient().getUser().getFullname());
        patientDto.setDateOfBirth(labTests.getRecord().getPatient().getUser().getDateOfBirth());
        dto.setPatient(patientDto);
        return dto;
    }
}
