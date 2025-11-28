package com.example.clinicbooking.service.ImagingTest;

import com.example.clinicbooking.DTO.ImagingTest.ImagingTestWaitingRequest;
import com.example.clinicbooking.DTO.LabTest.LabTestWaitingRequest;
import com.example.clinicbooking.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ImagingTestSpecification {
    public static Specification<ImagingTests> filterImagingTests(ImagingTestWaitingRequest request, String status, Integer ImagingStaffId) {
        return (root, query, criteriaBuilder) -> {

            Specification<ImagingTests> spec = Specification.where(null);

            //1.Tìm theo status
            if(status != null){
                spec = Specification.where(
                        (r, q, cb) -> cb.equal(r.get("status"), status)
                );
            }

            if(ImagingStaffId != null){
                //lọc theo kỹ thuật viên
                Join<ImagingTests, ImagingStaff> imagingStaffJoin = root.join("imagingStaff", JoinType.INNER);
                spec = spec.and((r, q, cb) -> cb.equal(imagingStaffJoin.get("id"), ImagingStaffId));
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

            Join<ImagingTests, Doctor> doctorJoin = root.join("doctor", JoinType.INNER);
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
                Join<ImagingTests, MedicalRecord> recordJoin = root.join("record", JoinType.INNER);
                Join<MedicalRecord, Patient> patientJoin = recordJoin.join("patient", JoinType.INNER);
                Join<Patient, User> userJoin = patientJoin.join("user", JoinType.INNER);

                // JOIN tới testType để tìm theo tên xét nghiệm
                Join<ImagingTests, ImagingTypes> imagingTypeJoin = root.join("imagingTypes",JoinType.INNER);

                Specification<ImagingTests> searchSpec = (rootSearch, querySearch, criteriaBuilderSearch) -> {
                    return criteriaBuilderSearch.or(
                            // Tìm theo Mã Bệnh nhân
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(patientJoin.get("patientCode")), likeQuery),
                            // Tìm theo Tên Bệnh nhân
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(userJoin.get("fullName")), likeQuery),
                            // Tìm theo tên xét nghiệm
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(imagingTypeJoin.get("imagingName")), likeQuery)
                    );
                };
                spec = spec.and(searchSpec);
            }

            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }

    // Hàm hỗ trợ tạo Specification để lọc trường LocalDateTime trong phạm vi một ngày.
    private static Specification<ImagingTests> filterByDateRange(LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            // Tính thời điểm bắt đầu của ngày (ví dụ: 2025-11-01 00:00:00)
            LocalDateTime startOfDay = date.atStartOfDay();
            // Tính thời điểm kết thúc của ngày (ví dụ: 2025-11-01 23:59:59.999...)
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

            return criteriaBuilder.between(root.get("requestedDate"), startOfDay, endOfDay);
        };
    }
}
