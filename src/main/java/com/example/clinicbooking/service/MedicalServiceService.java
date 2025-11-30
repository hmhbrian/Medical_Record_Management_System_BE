package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.MedicalService.*;
import com.example.clinicbooking.entity.ImagingTypes;
import com.example.clinicbooking.entity.Medical_Examination;
import com.example.clinicbooking.entity.TestTypes;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MedicalServiceService {
    private final TestTypeRepository testTypeRepo;
    private final ImagingTypeRepository imagingTypeRepos;
    private final MedicalExaminationRepository medicalExaminationRepo;
    private final DepartmentRepository departmentRepo;

    @Transactional
    public MedicalServiceOverviewResponse getOverview() {
        // Đếm từng danh mục
        long labCount  = testTypeRepo.count();
        long imgCount  = imagingTypeRepos.count();
        long exaCount  = medicalExaminationRepo.count();

        long total = labCount + imgCount + exaCount;

        // Đếm active (status = 1). Nếu boolean, dùng countByStatusTrue()
        long labActive = testTypeRepo.countByStatus(1);
        long imgActive = imagingTypeRepos.countByStatus(1);
        long exaActive = medicalExaminationRepo.countByStatus(1);
        long activeTotal = labActive + imgActive + exaActive;

        // Tổng giá để tính average toàn hệ
        double sumLab  = nullSafe(testTypeRepo.sumPrice());
        double sumImg  = nullSafe(imagingTypeRepos.sumPrice());
        double sumExa  = nullSafe(medicalExaminationRepo.sumPrice());
        double sumAll  = sumLab + sumImg + sumExa;

        double avg = total > 0 ? sumAll / total : 0.0;

        // Build response
        MedicalServiceOverviewResponse rp = new MedicalServiceOverviewResponse();
        rp.setTotalServices(total);
        rp.setActiveServices(activeTotal);
        rp.setAveragePrice(avg);

        rp.setExaminationCount(exaCount);
        rp.setLabTestCount(labCount);
        rp.setImagingCount(imgCount);

        return rp;
    }

    private double nullSafe(Double v) { return v == null ? 0.0 : v; }

    // ====== TẠO DỊCH VỤ mỚI ======
    @Transactional
    public boolean create(MedicalServiceRequest req) {
        var dept = departmentRepo.findById(req.getDepartment_id())
                .orElseThrow(() -> new InvalidInputException("Department not found: " + req.getDepartment_id()));

        try {
            switch (req.getMedicalService()) {
                case LAB_TEST -> {
                    TestTypes e = new TestTypes();
                    e.setTestName(req.getName());
                    e.setPrice(req.getPrice());
                    e.setStatus(req.getStatus());
                    e.setDescription(req.getDescription());
                    e.setDepartment(dept);
                    testTypeRepo.save(e);
                }
                case IMAGING -> {
                    ImagingTypes e = new ImagingTypes();
                    e.setImagingName(req.getName());
                    e.setPrice(req.getPrice());
                    e.setStatus(req.getStatus());
                    e.setDescription(req.getDescription());
                    e.setDepartment(dept);
                    imagingTypeRepos.save(e);
                }
                case EXAMINATION -> {
                    Medical_Examination e = new Medical_Examination();
                    e.setExaminationName(req.getName());
                    e.setPrice(req.getPrice());
                    e.setStatus(req.getStatus());
                    e.setDescription(req.getDescription());
                    e.setDepartment(dept);
                    medicalExaminationRepo.save(e);
                }
            }
            return true; // thành công
        } catch (Exception ex) {
            return false; // thất bại
        }
    }

    // ======LẤY DANH SÁCH======
    @Transactional
    public List<MedicalServiceResponse> Getlist(EMedicalService type) {
        if (type == null) {
            // gộp cả 3, rồi sort theo name
            return Stream.of(
                            testTypeRepo.findAll().stream().map(this::mapFromLab),
                            imagingTypeRepos.findAll().stream().map(this::mapFromImaging),
                            medicalExaminationRepo.findAll().stream().map(this::mapFromExam)
                    )
                    .flatMap(s -> s)
                    .sorted(Comparator.comparing(MedicalServiceResponse::getName, String.CASE_INSENSITIVE_ORDER))
                    .toList();
        }

        return switch (type) {
            case LAB_TEST -> testTypeRepo.findAll().stream().map(this::mapFromLab).toList();
            case IMAGING -> imagingTypeRepos.findAll().stream().map(this::mapFromImaging).toList();
            case EXAMINATION -> medicalExaminationRepo.findAll().stream().map(this::mapFromExam).toList();
        };
    }

    // ====== XEM CHI TIẾT ======
    @Transactional
    public MedicalServiceResponse getDetail(EMedicalService type, int id) {
        return switch (type) {
            case LAB_TEST -> mapFromLab(
                    testTypeRepo.findById(id)
                            .orElseThrow(() -> notFound("TestTypes", id))
            );
            case IMAGING -> mapFromImaging(
                    imagingTypeRepos.findById(id)
                            .orElseThrow(() -> notFound("ImagingTypes", id))
            );
            case EXAMINATION -> mapFromExam(
                    medicalExaminationRepo.findById(id)
                            .orElseThrow(() -> notFound("MedicalExamination", id))
            );
        };
    }
    // ====== MAPPERS ======
    private MedicalServiceResponse mapFromLab(TestTypes e) {
        MedicalServiceResponse r = new MedicalServiceResponse();
        r.setId(e.getId());
        r.setCode(e.getTestCode());
        r.setName(e.getTestName());
        r.setPrice(e.getPrice());
        r.setStatus(e.getStatus());
        r.setDescription(e.getDescription());
        r.setDepartmentName(e.getDepartment() != null ? e.getDepartment().getName() : null);
        r.setMedicalService(EMedicalService.LAB_TEST.getDescription());
        return r;
    }

    private MedicalServiceResponse mapFromImaging(ImagingTypes e) {
        MedicalServiceResponse r = new MedicalServiceResponse();
        r.setId(e.getId());
        r.setCode(e.getImagingCode());
        r.setName(e.getImagingName());
        r.setPrice(e.getPrice());
        r.setStatus(e.getStatus());
        r.setDescription(e.getDescription());
        r.setDepartmentName(e.getDepartment() != null ? e.getDepartment().getName() : null);
        r.setMedicalService(EMedicalService.IMAGING.getDescription());
        return r;
    }

    private MedicalServiceResponse mapFromExam(Medical_Examination e) {
        MedicalServiceResponse r = new MedicalServiceResponse();
        r.setId(e.getId());
        r.setCode(e.getExaminationCode());
        r.setName(e.getExaminationName());
        r.setStatus(e.getStatus());
        r.setPrice(e.getPrice());
        r.setDescription(e.getDescription());
        r.setDepartmentName(e.getDepartment() != null ? e.getDepartment().getName() : null);
        r.setMedicalService(EMedicalService.EXAMINATION.getDescription());
        return r;
    }

    // ====== CẬP NHẬT (partial) ======
    @Transactional
    public MedicalServiceResponse update(EMedicalService type, int id, UpdateMedicalServiceRequest req) {
        try {
            return switch (type) {
                case LAB_TEST -> {
                    TestTypes e = testTypeRepo.findById(id).orElseThrow(() -> notFound("TestTypes", id));
                    applyUpdateLab(e, req);
                    // kiểm tra trùng code/name (tránh unique violation)
                    if (req.getCode() != null && testTypeRepo.existsByTestCodeAndIdNot(req.getCode(), id))
                        conflict("Mã xét nghiệm đã tồn tại");
                    if (req.getName() != null && testTypeRepo.existsByTestNameAndIdNot(req.getName(), id))
                        conflict("Tên xét nghiệm đã tồn tại");
                    testTypeRepo.save(e);
                    yield mapFromLab(e);
                }
                case IMAGING -> {
                    ImagingTypes e = imagingTypeRepos.findById(id).orElseThrow(() -> notFound("ImagingTypes", id));
                    applyUpdateImaging(e, req);
                    if (req.getCode() != null && imagingTypeRepos.existsByImagingCodeAndIdNot(req.getCode(), id))
                        conflict("Mã chẩn đoán hình ảnh đã tồn tại");
                    if (req.getName() != null && imagingTypeRepos.existsByImagingNameAndIdNot(req.getName(), id))
                        conflict("Tên chẩn đoán hình ảnh đã tồn tại");
                    imagingTypeRepos.save(e);
                    yield mapFromImaging(e);
                }
                case EXAMINATION -> {
                    Medical_Examination e = medicalExaminationRepo.findById(id).orElseThrow(() -> notFound("MedicalExamination", id));
                    applyUpdateExam(e, req);
                     if (req.getCode() != null && medicalExaminationRepo.existsByExaminationCodeAndIdNot(req.getCode(), id))
                         conflict("Mã khám bệnh đã tồn tại");
                     if (req.getName() != null && medicalExaminationRepo.existsByExaminationNameAndIdNot(req.getName(), id))
                         conflict("Tên khám bệnh đã tồn tại");
                    medicalExaminationRepo.save(e);
                    yield mapFromExam(e);
                }
            };
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Trùng mã hoặc tên", ex);
        }
    }

    // ====== APPLY UPDATE (partial) ======
    private void applyUpdateLab(TestTypes e, UpdateMedicalServiceRequest r) {
        if (r.getCode() != null) e.setTestCode(r.getCode());
        if (r.getName() != null) e.setTestName(r.getName());
        if (r.getPrice() != null) e.setPrice(r.getPrice());
        if (r.getDescription() != null) e.setDescription(r.getDescription());
        if (r.getStatus() != null) e.setStatus(r.getStatus()); // int 0/1 (nếu boolean -> e.setStatus(r.getStatus()))
        if (r.getDepartment_id() != null) {
            var dept = departmentRepo.findById(r.getDepartment_id())
                    .orElseThrow(() -> new InvalidInputException("Department không tồn tại: " + r.getDepartment_id()));
            e.setDepartment(dept);
        }
    }

    private void applyUpdateImaging(ImagingTypes e, UpdateMedicalServiceRequest r) {
        if (r.getCode() != null) e.setImagingCode(r.getCode());
        if (r.getName() != null) e.setImagingName(r.getName());
        if (r.getPrice() != null) e.setPrice(r.getPrice());
        if (r.getDescription() != null) e.setDescription(r.getDescription());
        if (r.getStatus() != null) e.setStatus(r.getStatus());
        if (r.getDepartment_id() != null) {
            var dept = departmentRepo.findById(r.getDepartment_id())
                    .orElseThrow(() -> new InvalidInputException( "Department không tồn tại: " + r.getDepartment_id()));
            e.setDepartment(dept);
        }
    }

    private void applyUpdateExam(Medical_Examination e, UpdateMedicalServiceRequest r) {
        if (r.getCode() != null) e.setExaminationCode(r.getCode());
        if (r.getName() != null) e.setExaminationName(r.getName());
        if (r.getPrice() != null) e.setPrice(r.getPrice());
        if (r.getDescription() != null) e.setDescription(r.getDescription());
        if (r.getStatus() != null) e.setStatus(r.getStatus());
        if (r.getDepartment_id() != null) {
            var dept = departmentRepo.findById(r.getDepartment_id())
                    .orElseThrow(() -> new InvalidInputException("Department không tồn tại: " + r.getDepartment_id()));
            e.setDepartment(dept);
        }
    }

    // ====== helpers ======
    private InvalidInputException notFound(String entity, int id) {
        return new InvalidInputException(entity + " id=" + id + " không tồn tại");
    }

    private void conflict(String message) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, message);
    }

}
