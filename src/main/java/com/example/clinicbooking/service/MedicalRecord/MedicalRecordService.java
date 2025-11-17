package com.example.clinicbooking.service.MedicalRecord;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.MedicalRecord.*;
import com.example.clinicbooking.DTO.MedicalRecord.DiagnosisData.DiagnosisDataResponse;
import com.example.clinicbooking.DTO.MedicalRecord.DiagnosisData.DiagnosisUpdateRequest;
import com.example.clinicbooking.DTO.MedicalRecord.DiagnosisData.Icd10Response;
import com.example.clinicbooking.DTO.MedicalRecord.DiagnosisData.Icd10Request;
import com.example.clinicbooking.DTO.MedicalRecord.ServiceData.*;
import com.example.clinicbooking.DTO.PaginatedResponseDTO;
import com.example.clinicbooking.DTO.Patient.PatientSummary;
import com.example.clinicbooking.entity.*;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.*;
import com.example.clinicbooking.security.CustomUserDetails;
import com.example.clinicbooking.service.Payment.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {
    private final MedicalRecordRepository recordRepo;

    private final UserRepository userRepo;
    private final PatientRepository patientRepo;
    private final DoctorRepository doctorRepo;

    private final AppointmentRepository appointmentRepo;
    private final AppointmentStatusRepository appointmentStatusRepository;

    private final Icd10Repository icd10Repository;
    private final MedicalRecordIcd10Repository medicalRecordIcd10Repository;

    private final MedicalExaminationRepository medicalExaminationRepo;
    private final ResultExaminationRepository resultExaminationRepo;
    private final TestTypeRepository testTypeRepo;
    private final LabTestsRepository labTestRepo;
    private final ImagingTypeRepository imagingTypeRepo;
    private final ImagingTestsRepository imagingTestRepo;

    private final PaymentService paymentService;

    // Tạo mới hồ sơ ngoại trú
    public MedicalRecord CreateMedicalRecord(MedicalRecordRequest request) {
        Patient patient = patientRepo.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepo.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Appointment appointment = appointmentRepo.findById(request.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Kiểm tra xem đã có hồ sơ cho cuộc hẹn này chưa
        MedicalRecord existingRecord = recordRepo.findByAppointment(appointment)
                .orElse(null);
        if(existingRecord != null) {
            throw new InvalidInputException("Hồ sơ ngoại trú cho cuộc hẹn này đã tồn tại!");
        }
        MedicalRecord record = new MedicalRecord();

        record.setPatient(patient);
        record.setDoctor(doctor);
        record.setAppointment(appointment);
        record.setInitialSymptoms(request.getInitialSymptoms());
        record.setStatus(MedicalRecordStatus.WAITING);

        // Cập nhật trạng thái cuộc hẹn -> chờ khám
//        AppointmentStatus appointmentStatus = new AppointmentStatus();
//        appointmentStatus.setAppointment(appointment);
//        appointmentStatus.setStatus(3); // Chờ khám
//        appointmentStatus.setUpdateAt(LocalDateTime.now());
//        appointmentStatusRepository.save(appointmentStatus);

        return recordRepo.save(record);
    }

    // Hàm kiểm tra và cập nhật trạng thái Hồ sơ Ngoại trú khi tất cả dịch vụ đã hoàn thành
    public void checkAndTransitionRecordStatus(MedicalRecord record) {
        //Đếm tổng số dịch vụ đang chờ hoặc đang tiến hành
        long pendingCount = imagingTestRepo.countByRecordAndStatusIn(record, List.of(ServiceStatus.PENDING_PAYMENT, ServiceStatus.PAID, ServiceStatus.IN_PROGRESS));
        long pendingLabCount = labTestRepo.countByRecordAndStatusIn(record, List.of(ServiceStatus.PENDING_PAYMENT, ServiceStatus.PAID, ServiceStatus.IN_PROGRESS));

        long totalPending = pendingCount + pendingLabCount;

        if (totalPending == 0) {
            // Nếu không còn dịch vụ nào đang chờ/tiến hành
            if (record.getStatus() == MedicalRecordStatus.PENDING_RESULTS) {
                record.setStatus(MedicalRecordStatus.PENDING_APPROVAL);
                recordRepo.save(record);
            }
        }
    }

    // Lấy tất cả hồ sơ ngoại trú theo id bệnh nhân
    public List<MedicalRecordPatientResponse> getRecordsByPatientId(Integer patientId) {
        Patient patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new InvalidInputException("Patient not found with ID: " + patientId));

        return recordRepo.findByPatientId(patientId)
                .stream()
                .map(this::covertToPatientResponse)
                .collect(Collectors.toList());
    }

    // Lấy tất cả hồ sơ ngoại trú theo id bác sĩ
    public List<MedicalRecordResponse> getRecordsByDoctorId(Integer doctorId) {
        Doctor doctor = doctorRepo.findById(doctorId)
                .orElseThrow(() -> new InvalidInputException("Doctor not found with ID: " + doctorId));

        return recordRepo.findByDoctorId(doctorId)
                .stream()
                .map(this :: covertToResponse)
                .collect(Collectors.toList());
    }

    // Lấy tất cả hồ sơ ngoại trú, nhóm theo bệnh nhân
    public List<MedicalRecord> getAllRecordsGroupedByPatient() {
        return recordRepo.findAllGroupedByPatient();
    }

    // Lấy hồ sơ ngoại trú theo ID
    public Optional<MedicalRecord> getRecordById(Integer id) {
        MedicalRecord record = recordRepo.findById(id)
                .orElseThrow(() -> new InvalidInputException("Medical record not found with ID: " + id));

        return recordRepo.findById(id);
    }

    // Tìm kiếm và phân trang hồ sơ ngoại trú theo các tiêu chí
    public PaginatedResponseDTO<MedicalRecordResponse> searchRecords(
            MedicalRecordSearchRequest request) {
        //0.Lấy id doctor từ user đang đăng nhập
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }
        Integer doctorId = doctorRepo.findIdByUserId(cud.getId());

        // 1. Chuẩn bị phân trang và sắp xếp
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDir()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        // 2. Xây dựng Specification (logic lọc)
        Specification<MedicalRecord> spec = MedicalRecordSpecification.filterRecords(request, doctorId);

        // 3. Thực hiện truy vấn
        Page<MedicalRecord> recordsPage = recordRepo.findAll(spec, pageable);

        // 4. Ánh xạ (Mapping) Entity sang Response DTO
        List<MedicalRecordResponse> responseRecords = recordsPage.getContent().stream()
                .map(this::covertToResponse) // Sử dụng hàm covertToResponse để chuyển đổi
                .collect(Collectors.toList());

        // 5. Trả về Paginated Response
        return new PaginatedResponseDTO<MedicalRecordResponse>(
                recordsPage.getNumber(),
                recordsPage.getSize(),
                recordsPage.getTotalElements(),
                recordsPage.getTotalPages(),
                responseRecords
        );
    }

    // Thống kê hồ sơ ngoại trú theo ngày cho bác sĩ đang đăng nhập
    public MedicalRecordMetricsResponse getMetricsByDate(String dateString) {
        // Xác định ngày cần thống kê
        LocalDate currentDate = LocalDate.parse(dateString);

        //Lấy id doctor từ user đang đăng nhập
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }
        Integer doctorId = doctorRepo.findIdByUserId(cud.getId());

        // Thực hiện các truy vấn đếm
        // Tổng số bệnh nhân
        int total = recordRepo.countByDoctorIdAndVisitDate(doctorId, currentDate);

        // Đếm theo từng trạng thái cụ thể
        int pendingExam = recordRepo.countByDoctorIdAndVisitDateAndStatus(doctorId, currentDate, MedicalRecordStatus.WAITING);
        int inProgress = recordRepo.countByDoctorIdAndVisitDateAndStatus(doctorId, currentDate, MedicalRecordStatus.IN_PROGRESS);

        int pendingResult = recordRepo.countByDoctorIdAndVisitDateAndStatus(doctorId, currentDate, MedicalRecordStatus.PENDING_RESULTS);
        int pendingCompletion = recordRepo.countByDoctorIdAndVisitDateAndStatus(doctorId, currentDate, MedicalRecordStatus.COMPLETED);

        // Đóng gói dữ liệu vào DTO
        MedicalRecordMetricsResponse metrics = new MedicalRecordMetricsResponse();
        metrics.setTotalPatientsToday(total);
        metrics.setPendingExamCount(pendingExam);
        metrics.setInProgressCount(inProgress);
        metrics.setPendingResultCount(pendingResult);
        metrics.setPendingCompletionCount(pendingCompletion);

        return metrics;
    }

    // Cập nhật trạng thái hồ sơ ngoại trú
    public boolean UpdateMedicalRecordStatus(Integer recordId, String statusStr) {
        //Lấy id doctor từ user đang đăng nhập
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }

        MedicalRecord record = recordRepo.findById(recordId)
                .orElseThrow(() -> new InvalidInputException("Medical record not found with ID: " + recordId));

        // Cập nhật trạng thái hồ sơ ngoại trú -> đang khám
        MedicalRecordStatus status;
        try {
            status = MedicalRecordStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid status value: " + statusStr);
        }
        if(record.getStatus() == status || record.getStatus().ordinal() > status.ordinal()) {
            return false; // No update needed
        }
        record.setStatus(status);
        recordRepo.save(record);

        // Cập nhật trạng thái cuộc hẹn -> đang khám
        if(status == MedicalRecordStatus.IN_PROGRESS) {
            Appointment appointment = appointmentRepo.findById(record.getAppointment().getId())
                    .orElseThrow(() -> new InvalidInputException("Appointment not found with ID: " + record.getAppointment().getId()));
            User cudUser = userRepo.findById(cud.getId())
                    .orElseThrow(() -> new InvalidInputException("User not found with ID: " + cud.getId()));
            AppointmentStatus appointmentStatus = new AppointmentStatus();
            appointmentStatus.setAppointment(appointment);
            appointmentStatus.setStatus(4); // Đang khám
            appointmentStatus.setUpdateAt(LocalDateTime.now());
            appointmentStatus.setUpdate_by(cudUser);
            appointmentStatusRepository.save(appointmentStatus);
        }

        return true;
    }

    // Cập nhật chẩn đoán cho hồ sơ ngoại trú
    @Transactional // Đảm bảo tất cả các bước (Update, Delete, Save) là một giao dịch duy nhất
    public void updateDiagnosis(Integer recordId, DiagnosisUpdateRequest dto) {

        // 1. Tìm Hồ sơ Bệnh án hiện tại
        MedicalRecord record = recordRepo.findById(recordId)
                .orElseThrow(() -> new InvalidInputException("Hồ sơ bệnh án không tồn tại."));

        // 2. Cập nhật các trường cơ bản (Chẩn đoán văn bản, Ghi chú)
        record.setDiagnosis(dto.getDiagnosis());
        record.setNotes(dto.getNotes());
        if (record.getStatus() == MedicalRecordStatus.WAITING) {
            record.setStatus(MedicalRecordStatus.IN_PROGRESS);
        }

        // 3. Xử lý Dịch vụ Khám (Nếu có)
        if (dto.getExaminationServiceId() != null) {
            // Kiểm tra dịch vụ có tồn tại không
            Medical_Examination examination = medicalExaminationRepo.findById(dto.getExaminationServiceId())
                    .orElseThrow(() -> new InvalidInputException("Dịch vụ khám không tồn tại."));

            // Tạo hoặc cập nhật ResultExamination
            ResultExamination existingExamination = resultExaminationRepo.findByRecord(record)
                    .orElse(new ResultExamination());

            //ResultExamination newExamination = new ResultExamination();
            existingExamination.setRecord(record);
            existingExamination.setDoctor(record.getDoctor()); // Lấy bác sĩ từ hồ sơ
            existingExamination.setExamination(examination);
            existingExamination.setRequestedDate(LocalDateTime.now());
            existingExamination.setStatus(ServiceStatus.IN_PROGRESS); // Trạng thái: Chờ thanh toán

            resultExaminationRepo.save(existingExamination); //payment sẽ tạo khi có toa thuốc
        }


        // 4. Đồng bộ hóa Mã ICD-10

        //Xóa tất cả các chẩn đoán ICD-10 cũ của hồ sơ này
        medicalRecordIcd10Repository.deleteByRecordId(recordId);

        //Thêm các chẩn đoán ICD-10 mới từ DTO
        if (dto.getIcd10List() != null && !dto.getIcd10List().isEmpty()) {
            // Sắp xếp danh sách: Mã chính (true) lên đầu, Mã phụ (false) theo sau
            List<Icd10Request> sortedIcd10List = dto.getIcd10List().stream()
                    .sorted(Comparator.comparing(Icd10Request::isPrincipal).reversed())
                    .collect(Collectors.toList());

            final AtomicInteger orderCounter = new AtomicInteger(0);

            // Ánh xạ DTO sang Entity MedicalRecordIcd10
            List<MedicalRecordIcd10> newDiagnoses = dto.getIcd10List().stream()
                    .map(icd10Dto -> {
                        MedicalRecordIcd10 entity = new MedicalRecordIcd10();
                        entity.setRecord(record); // Liên kết với MedicalRecord

                        // Tìm icd10 Entity từ icd10CatalogId
                        Icd10 icd10 = icd10Repository.findById(icd10Dto.getIcd10CatalogId())
                                .orElseThrow(() -> new InvalidInputException("ICD-10 not found with ID: " + icd10Dto.getIcd10CatalogId()));
                        entity.setIcd10(icd10); // Tạm thời dùng ID
                        entity.setPrincipal(icd10Dto.isPrincipal());
                        entity.setDiagnosisOrder(orderCounter.getAndIncrement());
                        return entity;
                    })
                    .collect(Collectors.toList());

            medicalRecordIcd10Repository.saveAll(newDiagnoses);
        }

        // 5. Lưu cập nhật cho Hồ sơ chính
        recordRepo.save(record);
    }

    // Lấy dữ liệu chẩn đoán cho giao diện người dùng
    public DiagnosisDataResponse getDiagnosisData(Integer recordId) {

        // 1. Lấy Medical Record, Patient và User (tối ưu bằng JOIN)
        MedicalRecord record = recordRepo.findByIdWithPatientAndUser(recordId)
                .orElseThrow(() -> new InvalidInputException(
                        "Hồ sơ bệnh án có ID " + recordId + " không tồn tại. (Lỗi 404)"
                ));

        Patient patient = record.getPatient();
        User user = patient.getUser();

        // 2. Lấy danh sách ICD-10 đã được chẩn đoán
        List<MedicalRecordIcd10> savedIcd10s = medicalRecordIcd10Repository.findByRecordIdOrderByDiagnosisOrder(recordId);

        // 3. Xử lý Dịch vụ Khám (Kiểm tra xem đã chỉ định dịch vụ khám chưa)
        Optional<ResultExamination> optionalExamResult = resultExaminationRepo.findByRecord(record);
        Integer examinationServiceId = null;
        String examinationServiceName = null;

        if(optionalExamResult.isPresent()) {
            ResultExamination examResult = optionalExamResult.get();
            examinationServiceId = examResult.getExamination().getId();
            examinationServiceName = examResult.getExamination().getExaminationName();
        }

        // 4. Ánh xạ (Mapping) sang Response DTO
        DiagnosisDataResponse dto = new DiagnosisDataResponse();

        // Gán thông tin hồ sơ
        dto.setRecordId(record.getId());
        dto.setInitialSymptoms(record.getInitialSymptoms());
        dto.setNotes(record.getNotes());
        dto.setDiagnosis(record.getDiagnosis());
        dto.setMedicalHistory(patient.getMedicalHistory()); // Lấy từ Patient

        //Thông tin bệnh nhân
        PatientSummary patientSummary = new PatientSummary();
        patientSummary.setPatientCode(patient.getPatientCode());
        patientSummary.setFullName(user.getFullname());
        patientSummary.setDateOfBirth(user.getDateOfBirth());
        patientSummary.setPhoneNumber(user.getPhoneNumber());
        dto.setPatient(patientSummary);

        // Gán ICD-10s
        List<Icd10Response> icd10Dtos = savedIcd10s.stream()
                .map(this::convertToIcd10Dto)
                .collect(Collectors.toList());
        dto.setIcd10Diagnoses(icd10Dtos);

        // Gán thông tin Dịch vụ Khám
        dto.setExaminationServiceId(examinationServiceId);
        dto.setExaminationServiceName(examinationServiceName);

        return dto;
    }

    // Hàm chuyển đổi Entity MedicalRecordIcd10 sang DTO
    private Icd10Response convertToIcd10Dto(MedicalRecordIcd10 entity) {
        Icd10Response dto = new Icd10Response();
        dto.setId(entity.getIcd10().getId());
        dto.setCode(entity.getIcd10().getCode());
        dto.setName(entity.getIcd10().getNameVn());
        dto.setPrincipal(entity.isPrincipal());
        dto.setDiagnosisOrder(entity.getDiagnosisOrder());
        return dto;
    }

    // Tạo Chỉ định Dịch vụ (Xét nghiệm + Hình ảnh) cho hồ sơ bệnh án
    @Transactional
    public ApiResponse<?> createServiceOrders(Integer recordId, ServiceOrdersRequest dto) {

        MedicalRecord record = recordRepo.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Hồ sơ bệnh án không tồn tại."));

        // Danh sách các ID dịch vụ (Service ID) cần thanh toán
        List<ServiceDetail> serviceItems = new ArrayList<>();

        // --- 1. XỬ LÝ CHỈ ĐỊNH XÉT NGHIỆM (LAB TESTS) ---
        if (dto.getLabTestCatalogIds() != null && !dto.getLabTestCatalogIds().isEmpty()) {
            for (Integer catalogId : dto.getLabTestCatalogIds()) {
                TestTypes testTypes = testTypeRepo.findById(catalogId)
                        .orElseThrow(() -> new RuntimeException("Mã xét nghiệm không hợp lệ: " + catalogId));

                // A. Tạo bản ghi LabTest
                LabTests labTest = new LabTests();
                labTest.setRecord(record);
                labTest.setDoctor(record.getDoctor());
                labTest.setTestTypes(testTypes); // Liên kết tới loại xét nghiệm
                labTest.setRequestedDate(LocalDateTime.now());
                labTest.setStatus(ServiceStatus.PENDING_PAYMENT); // Trạng thái ban đầu

                labTest = labTestRepo.save(labTest);

                // B. Thêm vào danh sách thanh toán
                serviceItems.add(new ServiceDetail("LAB_TEST", labTest.getId(), testTypes.getTestName(), testTypes.getPrice()));
           }
        }

        // --- 2. XỬ LÝ CHỈ ĐỊNH CHẨN ĐOÁN HÌNH ẢNH (IMAGING) ---
        if (dto.getImagingTypeIds() != null && !dto.getImagingTypeIds().isEmpty()) {
            for (Integer typeId : dto.getImagingTypeIds()) {
                ImagingTypes imagingType = imagingTypeRepo.findById(typeId)
                        .orElseThrow(() -> new RuntimeException("Id loại chẩn đooán hình ảnh không hợp lệ: " + typeId));

                // A. Tạo bản ghi ImagingTest
                ImagingTests imagingTest = new ImagingTests();
                imagingTest.setRecord(record);
                imagingTest.setDoctor(record.getDoctor());
                imagingTest.setImagingTypes(imagingType); // Liên kết tới loại hình ảnh
                imagingTest.setRequestedDate(LocalDateTime.now());
                imagingTest.setStatus(ServiceStatus.PENDING_PAYMENT);

                imagingTest = imagingTestRepo.save(imagingTest);

                // B. Thêm vào danh sách thanh toán
                serviceItems.add(new ServiceDetail("IMAGING_TEST", imagingTest.getId(), imagingType.getImagingName(), imagingType.getPrice()));
            }
        }

        // --- 3. Tạo phiếu THANH TOÁN TRẢ TRƯỚC (chỉ cho Lab/Imaging) ---
        if (serviceItems.isEmpty()) {
            // Trường hợp không có chỉ định nào được gửi
            return new ApiResponse<>(false, "Không có dịch vụ nào được chỉ định.", null);
        }

        paymentService.createPaymentOrder(record, serviceItems);

        // Cập nhật trạng thái hồ sơ (Ví dụ: Chuyển từ IN_PROGRESS sang PENDING_PAYMENT_SERVICE)
        record.setStatus(MedicalRecordStatus.PENDING_PREPAYMENT);
        recordRepo.save(record);

        return new ApiResponse<>(true, "Chỉ định đã được gửi thành công và chuyển sang thanh toán.",null);
    }

    // Lấy danh sách Chỉ định Dịch vụ (Xét nghiệm + Hình ảnh) cho hồ sơ bệnh án
    public List<ServiceOrderResponse> getServiceOrders(Integer recordId) {
        MedicalRecord record = recordRepo.findById(recordId)
                .orElseThrow(() -> new InvalidInputException("Hồ sơ bệnh án không tồn tại."));

        // 1. Lấy danh sách Chỉ định Xét nghiệm
        List<LabTests> labTests = labTestRepo.findAllByRecord(record);

        // 2. Lấy danh sách Chỉ định Hình ảnh
        List<ImagingTests> imagingTests = imagingTestRepo.findAllByRecord(record);

        if(labTests.isEmpty() && imagingTests.isEmpty()) {
            return new ArrayList<>(); // Trả về danh sách rỗng nếu không có chỉ định
        }
        // 3. Ánh xạ sang DTO
        Stream<ServiceOrderResponse> labTestDtos = labTests.stream()
                .map(this::mapLabTestToUnifiedDTO);

        Stream<ServiceOrderResponse> imagingTestDtos = imagingTests.stream()
                .map(this::mapImagingTestToUnifiedDTO);

        // 4. Hợp nhất hai Stream và sắp xếp theo ngày yêu cầu
        return Stream.concat(labTestDtos, imagingTestDtos)
                .sorted(Comparator.comparing(ServiceOrderResponse::getRequestDate).reversed())
                .collect(Collectors.toList());
    }

    // Hàm ánh xạ LabTest Entity sang DTO
    private ServiceOrderResponse mapLabTestToUnifiedDTO(LabTests lt) {
        ServiceOrderResponse dto = new ServiceOrderResponse();
        dto.setOrderId(lt.getId());
        dto.setOrderType("LAB_TEST");
        dto.setCode(lt.getTestTypes().getTestCode());
        dto.setName(lt.getTestTypes().getTestName());
        dto.setRequestDate(lt.getRequestedDate());
        dto.setResultDate(lt.getResultDate());
        dto.setResult(lt.getResult());
        dto.setStatus(lt.getStatus().name());

        // Tên nhân viên phụ trách
        if (lt.getLabTechnician() != null && lt.getLabTechnician().getStaff().getUser() != null) {
            dto.setAssignedStaffName(lt.getLabTechnician().getStaff().getUser().getFullname());
            dto.setAssignedStaffCode(lt.getLabTechnician().getLabTechnicianCode());
        } else {
            dto.setAssignedStaffName("Chưa phân công");
        }

        return dto;
    }

    // Hàm ánh xạ ImagingTest Entity sang DTO (Tương tự LabTest)
    private ServiceOrderResponse mapImagingTestToUnifiedDTO(ImagingTests it) {
        ServiceOrderResponse dto = new ServiceOrderResponse();
        dto.setOrderId(it.getId());
        dto.setOrderType("IMAGING_TEST");
        dto.setCode(it.getImagingTypes().getImagingCode());
        dto.setName(it.getImagingTypes().getImagingName());
        dto.setRequestDate(it.getRequestedDate());
        dto.setResultDate(it.getResultDate());
        dto.setResult(it.getResult());
        dto.setStatus(it.getStatus().name());

        // Tên nhân viên phụ trách
        if (it.getImagingStaff() != null && it.getImagingStaff().getStaff().getUser() != null) {
            dto.setAssignedStaffName(it.getImagingStaff().getStaff().getUser().getFullname());
            dto.setAssignedStaffCode(it.getImagingStaff().getImgScode());
        } else {
            dto.setAssignedStaffName("Chưa phân công");
        }
        return dto;
    }

    // Chuyển đổi MedicalRecord từ Entity sang DTO Response
    private MedicalRecordResponse covertToResponse(MedicalRecord medicalRecord) {
        PatientSummary patientSummary = new PatientSummary();
        patientSummary.setPatientCode(medicalRecord.getPatient().getPatientCode());
        patientSummary.setDateOfBirth(medicalRecord.getPatient().getUser().getDateOfBirth());
        patientSummary.setFullName(medicalRecord.getPatient().getUser().getFullname());
        patientSummary.setPhoneNumber(medicalRecord.getPatient().getUser().getPhoneNumber());

        MedicalRecordResponse dto = new MedicalRecordResponse();
        dto.setRecordId(medicalRecord.getId());
        dto.setRecordCode(medicalRecord.getCode());
        dto.setInitialSymptoms(medicalRecord.getInitialSymptoms());
        dto.setDiagnosis(medicalRecord.getDiagnosis());
        dto.setVisitNumber(medicalRecord.getAppointment().getVisitNumber());
        dto.setVisitDate(medicalRecord.getAppointment().getVisitDateTime());
        dto.setAppointmentId(medicalRecord.getAppointment().getId());
        dto.setPatient(patientSummary);
        dto.setStatus(medicalRecord.getStatus().name());
        return dto;
    }

    private MedicalRecordPatientResponse covertToPatientResponse(MedicalRecord medicalRecord) {
        MedicalRecordPatientResponse dto = new MedicalRecordPatientResponse();
        dto.setRecordId(medicalRecord.getId());
        dto.setRecordCode(medicalRecord.getCode());
        dto.setInitialSymptoms(medicalRecord.getInitialSymptoms());
        dto.setDiagnosis(medicalRecord.getDiagnosis());
        dto.setVisitNumber(medicalRecord.getAppointment().getVisitNumber());
        dto.setVisitDate(medicalRecord.getAppointment().getVisitDateTime());
        dto.setAppointmentId(medicalRecord.getAppointment().getId());
        dto.setStatus(medicalRecord.getStatus().name());

        dto.setDoctorName(medicalRecord.getDoctor().getStaff().getUser().getFullname());
        dto.setDoctorSpecialty(medicalRecord.getDoctor().getSpecialty().getName());

        return dto;
    }

}

