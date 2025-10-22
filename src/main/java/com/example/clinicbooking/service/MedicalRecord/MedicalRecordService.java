package com.example.clinicbooking.service.MedicalRecord;

import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordMetricsResponse;
import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordRequest;
import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordResponse;
import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordSearchRequest;
import com.example.clinicbooking.DTO.PaginatedResponseDTO;
import com.example.clinicbooking.DTO.Patient.PatientSummary;
import com.example.clinicbooking.entity.*;
import com.example.clinicbooking.repository.AppointmentRepository;
import com.example.clinicbooking.repository.DoctorRepository;
import com.example.clinicbooking.repository.MedicalRecordRepository;
import com.example.clinicbooking.repository.PatientRepository;
import com.example.clinicbooking.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService {
    @Autowired
    private MedicalRecordRepository recordRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

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
        record.setDiagnosis(request.getDiagnosis());
        record.setStatus(status);

        return recordRepo.save(record);
    }

    public List<MedicalRecordResponse> getRecordsByPatientId(Integer patientId) {
        return recordRepo.findByPatientId(patientId)
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    public List<MedicalRecord> getRecordsByDoctorId(Integer doctorId) {
        return recordRepo.findByDoctorId(doctorId);
    }

    public List<MedicalRecord> getAllRecordsGroupedByPatient() {
        return recordRepo.findAllGroupedByPatient();
    }

    public Optional<MedicalRecord> getRecordById(Integer id) {
        return recordRepo.findById(id);
    }

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

    public boolean UpdateMedicalRecordStatus(Integer recordId, String statusStr) {
        MedicalRecord record = recordRepo.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Medical record not found"));

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
        return true;
    }

    private MedicalRecordResponse covertToResponse(MedicalRecord medicalRecord) {
        PatientSummary patientSummary = new PatientSummary();
        patientSummary.setPatientCode(medicalRecord.getPatient().getPatientcode());
        patientSummary.setDateOfBirth(medicalRecord.getPatient().getUser().getDateOfBirth());
        patientSummary.setFullName(medicalRecord.getPatient().getUser().getFullname());
        patientSummary.setPhoneNumber(medicalRecord.getPatient().getUser().getPhoneNumber());

        MedicalRecordResponse dto = new MedicalRecordResponse();
        dto.setRecordId(medicalRecord.getId());
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

