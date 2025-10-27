package com.example.clinicbooking.service.MedicalRecord;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.MedicalRecord.DiagnosisData.DiagnosisDataResponse;
import com.example.clinicbooking.DTO.MedicalRecord.DiagnosisData.DiagnosisUpdateRequest;
import com.example.clinicbooking.DTO.MedicalRecord.DiagnosisData.Icd10Response;
import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordMetricsResponse;
import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordRequest;
import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordResponse;
import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordSearchRequest;
import com.example.clinicbooking.DTO.MedicalRecord.DiagnosisData.Icd10Request;
import com.example.clinicbooking.DTO.MedicalRecord.ServiceData.ServiceDetail;
import com.example.clinicbooking.DTO.MedicalRecord.ServiceData.ServiceOrdersRequest;
import com.example.clinicbooking.DTO.PaginatedResponseDTO;
import com.example.clinicbooking.DTO.Patient.PatientSummary;
import com.example.clinicbooking.entity.*;
import com.example.clinicbooking.repository.*;
import com.example.clinicbooking.security.CustomUserDetails;
import com.example.clinicbooking.service.MedicalExamination.MedicalExaminationService;
import com.example.clinicbooking.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

    private final PaymentRepository paymentRepo;
    private final PaymentDetailRepository paymentDetailRepo;
    private final PaymentService paymentService;

    // Tạo mới hồ sơ ngoại trú
    public MedicalRecord CreateMedicalRecord(MedicalRecordRequest request) {
        MedicalRecord record = new MedicalRecord();

        Patient patient = patientRepo.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepo.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Appointment appointment = appointmentRepo.findById(request.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        LocalDate today = LocalDate.now();
        int visitNumber = recordRepo.countVisitNumber(doctor.getId(), today) + 1;
        MedicalRecordStatus status = MedicalRecordStatus.WAITING;

        record.setPatient(patient);
        record.setDoctor(doctor);
        record.setAppointment(appointment);
        record.setVisitDate(today);
        record.setVisitNumber(visitNumber);
        record.setInitialSymptoms(request.getInitialSymptoms());
        record.setStatus(status);

        // Cập nhật trạng thái cuộc hẹn -> chờ khám
        AppointmentStatus appointmentStatus = new AppointmentStatus();
        appointmentStatus.setAppointment(appointment);
        appointmentStatus.setStatus(3); // Chờ khám
        appointmentStatus.setUpdateAt(LocalDateTime.now());
        appointmentStatusRepository.save(appointmentStatus);

        return recordRepo.save(record);
    }

    // Lấy tất cả hồ sơ ngoại trú theo id bệnh nhân
    public List<MedicalRecordResponse> getRecordsByPatientId(Integer patientId) {
        return recordRepo.findByPatientId(patientId)
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    // Lấy tất cả hồ sơ ngoại trú theo id bác sĩ
    public List<MedicalRecordResponse> getRecordsByDoctorId(Integer doctorId) {
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
                .orElseThrow(() -> new RuntimeException("Medical record not found"));

        // Cập nhật trạng thái hồ sơ ngoại trú -> đang khám
        MedicalRecordStatus status;
        try {
            status = MedicalRecordStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status value: " + statusStr);
        }
        if(record.getStatus() == status || record.getStatus().ordinal() > status.ordinal()) {
            return false; // No update needed
        }
        record.setStatus(status);
        recordRepo.save(record);

        // Cập nhật trạng thái cuộc hẹn -> đang khám
        if(status == MedicalRecordStatus.IN_PROGRESS) {
            Appointment appointment = appointmentRepo.findById(record.getAppointment().getId())
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));
            User cudUser = userRepo.findById(cud.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
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
                .orElseThrow(() -> new RuntimeException("Hồ sơ bệnh án không tồn tại."));

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
                    .orElseThrow(() -> new RuntimeException("Dịch vụ khám không tồn tại."));

            //Tạo bản ghi mới trong bảng 'resultexamination'
            ResultExamination newExamination = new ResultExamination();
            newExamination.setRecord(record);
            newExamination.setDoctor(record.getDoctor()); // Lấy bác sĩ từ hồ sơ
            newExamination.setExamination(examination);
            newExamination.setRequestedDate(LocalDateTime.now());
            newExamination.setStatus("PENDING_PAYMENT"); // Trạng thái: Chờ thanh toán

            newExamination = resultExaminationRepo.save(newExamination);

            //Tạo payment
            List<ServiceDetail> serviceItems = List.of(
                    new ServiceDetail("EXAMINATION", newExamination.getId(), examination.getExaminationName(), examination.getPrice())
            );
            paymentService.handlePayment(record, serviceItems);
//            BigDecimal servicePrice = new BigDecimal(String.valueOf(examination.getPrice()));
//
//            double insuranceRateService = 0.8; // Giả định tỷ lệ hỗ trợ BHYT
//            double insuranceRatePatient = record.getPatient().getInsuranceRate(); // Lấy tỷ lệ BHYT của bệnh nhân
//
//            // Lấy tỷ lệ hỗ trợ BHYT áp dụng thấp hơn giữa dịch vụ và bệnh nhân
//            double finalRateDouble = Math.min(insuranceRateService, insuranceRatePatient);
//
//            //Dùng BigDecimal để tính toán chính xác tiền tệ
//            BigDecimal insuranceRate = new BigDecimal(finalRateDouble);
//
//            //Đặt scale cho tiền tệ: scale = 0 (cho VND), 2 (cho USD/EUR)
//            int scale = 0;
//            RoundingMode roundingMode = RoundingMode.HALF_UP;
//
//            // Tính số tiền BHYT chi trả
//            BigDecimal insuranceCovered = servicePrice
//                    .multiply(insuranceRate)
//                    .setScale(scale, roundingMode); // Áp dụng làm tròn
//
//            // Tính số tiền bệnh nhân phải trả
//            BigDecimal patientOwed = servicePrice.subtract(insuranceCovered);
//
//            //Tạo bản ghi Payment
//            Payment payment = new Payment();
//            payment.setRecord(record);
//            payment.setTotalAmount(servicePrice);
//            payment.setInsuranceCoverage(insuranceCovered); // Gán tổng tiền BHYT ước tính
//            payment.setPatientPayment(patientOwed);       // Gán tổng tiền bệnh nhân phải trả ước tính
//            payment.setStatus("PENDING_PAYMENT");
//            payment.setCreatedAt(LocalDateTime.now());
//
//            payment = paymentRepo.save(payment);
//
//            //Tạo bản ghi PaymentDetail
//            PaymentDetail detail = new PaymentDetail();
//            detail.setPayment(payment);
//            detail.setServiceType("EXAMINATION");
//            detail.setServiceId(newExamination.getId());
//            detail.setDescription(examination.getExaminationName());
//            detail.setTotalAmount(servicePrice);
//            detail.setCreatedAt(LocalDateTime.now());
//
//            // Gán chi tiết tiền BHYT và bệnh nhân phải trả cho từng mục
//            detail.setInsuranceCoveredAmount(insuranceCovered);
//            detail.setPatientPaidAmount(patientOwed);
//            paymentDetailRepo.save(detail);
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
                                .orElseThrow(() -> new RuntimeException("ICD-10 not found with ID: " + icd10Dto.getIcd10CatalogId()));
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
                .orElseThrow(() -> new RuntimeException("Hồ sơ bệnh án không tồn tại."));

        Patient patient = record.getPatient();
        User user = patient.getUser();

        // 2. Lấy danh sách ICD-10 đã được chẩn đoán
        List<MedicalRecordIcd10> savedIcd10s = medicalRecordIcd10Repository.findByRecordIdOrderByDiagnosisOrder(recordId);

        // 3. Xử lý Dịch vụ Khám (Kiểm tra xem đã chỉ định dịch vụ khám chưa)
        ResultExamination examResult = resultExaminationRepo.findByRecord(record)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy dịch vụ khám cho hồ sơ này."));

        Integer examinationServiceId = null;
        String examinationServiceName = null;
        if(examResult != null) {
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
        patientSummary.setPatientCode(patient.getPatientcode());
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
                labTest.setStatus("PENDING_PAYMENT"); // Trạng thái ban đầu

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
                imagingTest.setStatus("PENDING_PAYMENT");

                imagingTest = imagingTestRepo.save(imagingTest);

                // B. Thêm vào danh sách thanh toán
                serviceItems.add(new ServiceDetail("IMAGING_TEST", imagingTest.getId(), imagingType.getImagingName(), imagingType.getPrice()));
            }
        }

        // --- 3. TẠO PHIẾU THANH TOÁN (PAYMENTS) ---
        if (serviceItems.isEmpty()) {
            // Trường hợp không có chỉ định nào được gửi
            return new ApiResponse<>(false, "Không có dịch vụ nào được chỉ định.", null);
        }

        paymentService.handlePayment(record, serviceItems);

        // Cập nhật trạng thái hồ sơ (Ví dụ: Chuyển từ IN_PROGRESS sang PENDING_PAYMENT_SERVICE)
        record.setStatus(MedicalRecordStatus.PENDING_RESULTS);
        recordRepo.save(record);

        return new ApiResponse<>(true, "Chỉ định đã được gửi thành công và chuyển sang thanh toán.",null);
    }

    // Chuyển đổi từ Entity sang DTO Response
    private MedicalRecordResponse covertToResponse(MedicalRecord medicalRecord) {
        PatientSummary patientSummary = new PatientSummary();
        patientSummary.setPatientCode(medicalRecord.getPatient().getPatientcode());
        patientSummary.setDateOfBirth(medicalRecord.getPatient().getUser().getDateOfBirth());
        patientSummary.setFullName(medicalRecord.getPatient().getUser().getFullname());
        patientSummary.setPhoneNumber(medicalRecord.getPatient().getUser().getPhoneNumber());

        MedicalRecordResponse dto = new MedicalRecordResponse();
        dto.setRecordId(medicalRecord.getId());
        dto.setRecordCode(medicalRecord.getCode());
        dto.setInitialSymptoms(medicalRecord.getInitialSymptoms());
        dto.setDiagnosis(medicalRecord.getDiagnosis());
        dto.setVisitNumber(medicalRecord.getVisitNumber());
        dto.setVisitDate(medicalRecord.getVisitDate());
        dto.setAppointmentId(medicalRecord.getAppointment().getId());
        dto.setPatient(patientSummary);
        dto.setStatus(medicalRecord.getStatus().name());
        return dto;
    }

}

