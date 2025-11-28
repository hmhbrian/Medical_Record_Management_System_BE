package com.example.clinicbooking.service.LabTest;

import com.example.clinicbooking.DTO.LabTest.LabTestWaitingRequest;
import com.example.clinicbooking.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

//lọc theo ngày hiện tại, trạng thái(bắt buộc), tên xét nghiệm, chuyên khoa
public class LabTestSpecification {
    public static Specification<LabTests> filterLabTests(LabTestWaitingRequest request, String status, Integer LabStaffId) {
        return (root, query, criteriaBuilder) -> {
            // KHỞI TẠO spec MẶC ĐỊNH LÀ TRUE
            Specification<LabTests> spec = Specification.where(null);
            //Tìm theo status
            if(status != null) {
                spec = spec.and((r, q, cb) -> cb.equal(r.get("status"), status));
            }

            if(LabStaffId != null){
                //lọc theo kỹ thuật viên
                Join<LabTests, LabTechnician> labTechJoin = root.join("labTechnician", JoinType.INNER);
                spec = spec.and((r, q, cb) -> cb.equal(labTechJoin.get("id"), LabStaffId));
            }

            // Lấy ngày hiện tại để làm mặc định nếu không có ngày tìm kiếm
            LocalDate defaultDate = LocalDate.now();

            //Xác định ngày tìm kiếm (Search Date)
            // Nếu request.getCreatedAt() có giá trị, dùng nó. Ngược lại, dùng ngày hiện tại.
            LocalDate searchLocalDate;
            if (request.getFindDate() != null && !request.getFindDate().isEmpty()) {
                try {
                    // Chuyển chuỗi ngày thành LocalDate
                    searchLocalDate = LocalDate.parse(request.getFindDate(), DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (Exception e) {
                    // Xử lý lỗi hoặc sử dụng ngày mặc định nếu định dạng sai
                    searchLocalDate = defaultDate;
                }
            } else {
                // Nếu không có ngày tìm kiếm, sử dụng ngày hiện tại (hoặc bỏ qua lọc nếu không muốn mặc định)
                searchLocalDate = defaultDate;
            }

            // 2. Lọc theo Ngày
            spec = spec.and(filterByDateRange(searchLocalDate));

            //3. lọc theo bác sĩ

            Join<LabTests, Doctor> doctorJoin = root.join("doctor", JoinType.INNER);
            if(request.getDoctorId() != null){
                spec = spec.and((r, q, cb) -> cb.equal(doctorJoin.get("id"), request.getDoctorId()));

            }

            //4. lọc theo chuyên khoa
            Join<Doctor, Specialty> specialtyJoin = doctorJoin.join("specialty", JoinType.INNER);
            if(request.getSpecialtyId() != null){
                spec = spec.and((r, q, cb) -> cb.equal(specialtyJoin.get("id"), request.getSpecialtyId()));
            }

            // 4. Tìm kiếm chung (Query)
            if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
                String likeQuery = "%" + request.getKeyword().toLowerCase() + "%";

                // JOIN tới Patient và User để tìm theo Tên và Mã bệnh nhân
                Join<LabTests, MedicalRecord> recordJoin = root.join("record", JoinType.INNER);
                Join<MedicalRecord, Patient> patientJoin = recordJoin.join("patient", JoinType.INNER);
                Join<Patient, User> userJoin = patientJoin.join("user", JoinType.INNER);

                // JOIN tới testType để tìm theo tên xét nghiệm
                Join<LabTests, TestTypes> testTypeJoin = root.join("testTypes",JoinType.INNER);

                Specification<LabTests> searchSpec = (rootSearch, querySearch, criteriaBuilderSearch) -> {
                    return criteriaBuilderSearch.or(
                            // Tìm theo Mã Bệnh nhân
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(patientJoin.get("patientCode")), likeQuery),
                            // Tìm theo Tên Bệnh nhân
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(userJoin.get("fullName")), likeQuery),
                            // Tìm theo tên xét nghiệm
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(testTypeJoin.get("testName")), likeQuery)
                    );
                };
                spec = spec.and(searchSpec);
            }

            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }

    // Hàm hỗ trợ tạo Specification để lọc trường LocalDateTime trong phạm vi một ngày.
    private static Specification<LabTests> filterByDateRange(LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            // Tính thời điểm bắt đầu của ngày (ví dụ: 2025-11-01 00:00:00)
            LocalDateTime startOfDay = date.atStartOfDay();
            // Tính thời điểm kết thúc của ngày (ví dụ: 2025-11-01 23:59:59.999...)
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

            return criteriaBuilder.between(root.get("requestedDate"), startOfDay, endOfDay);
        };
    }
}
