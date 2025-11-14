package com.example.clinicbooking.service.Appointment;

import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordSearchRequest;
import com.example.clinicbooking.entity.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class QueueSpecification {
    public static Specification<Appointment> filterQueue(String keyword, Integer speacialtyId, Integer status, String FindDate, String visitType) {
        return (root, query, criteriaBuilder) -> {

            Specification<Appointment> spec = Specification.where(null); // Khởi tạo với 'where(null)'

            // 1. Lọc theo visitType - CHỈ LỌC NẾU CÓ GIÁ TRỊ
            if (visitType != null && !visitType.isEmpty()) {
                spec = spec.and((r, q, cb) -> cb.equal(root.get("visitType"), visitType));
            }

            // 2. Lọc theo chuyên khoa
            Join<Appointment, Doctor> doctorJoin = root.join("doctor", JoinType.INNER);
            Join<Doctor, Specialty> specialtyJoin = doctorJoin.join("specialty", JoinType.INNER);
            if (speacialtyId != null) {
                spec = spec.and((r, q, cb) -> cb.equal(specialtyJoin.get("id"), speacialtyId));
            }

            //Lọc theo ngày checkin
            // Lấy ngày hiện tại để làm mặc định nếu không có ngày tìm kiếm
            LocalDate defaultDate = LocalDate.now();

            //Xác định ngày tìm kiếm (Search Date)
            // Nếu FindDate có giá trị, dùng nó. Ngược lại, dùng ngày hiện tại.
            LocalDate searchLocalDate;
            if (FindDate != null && !FindDate.isEmpty()) {
                try {
                    // Chuyển chuỗi ngày thành LocalDate
                    searchLocalDate = LocalDate.parse(FindDate, DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (Exception e) {
                    // Xử lý lỗi hoặc sử dụng ngày mặc định nếu định dạng sai
                    searchLocalDate = defaultDate;
                }
            } else {
                // Nếu không có ngày tìm kiếm, sử dụng ngày hiện tại (hoặc bỏ qua lọc nếu không muốn mặc định)
                searchLocalDate = defaultDate;
            }

            // 3. Lọc theo Ngày checkin
            spec = spec.and(filterByDateRange(searchLocalDate));

            // 4. Lọc theo Trạng thái của bản ghi AppointmentStatus MỚI NHẤT
            if (status > 0) {
                // Tạo Subquery để tìm trạng thái mới nhất (MAX(id) của AppointmentStatus)
                Subquery<Integer> subquery = query.subquery(Integer.class);
                Root<AppointmentStatus> statusRoot = subquery.from(AppointmentStatus.class);

                //Chỉ định rõ kiểu dữ liệu khi gọi .get("id")
                Path<Integer> statusIdPath = statusRoot.get("id");

                // Grouping theo AppointmentID
                subquery.groupBy(statusRoot.get("appointment").get("id"));

                // Chọn ID lớn nhất (id mới nhất) của AppointmentStatus cho mỗi Appointment
                subquery.select(criteriaBuilder.greatest(statusIdPath)).where(
                        criteriaBuilder.equal(statusRoot.get("appointment").get("id"), root.get("id"))
                );

                // Sau đó, JOIN AppointmentStatus vào Main Query (chỉ những bản ghi có ID lớn nhất)
                Join<Appointment, AppointmentStatus> latestStatusJoin = root.join("appointmentStatuses", JoinType.INNER);

                // Lọc điều kiện: AppointmentStatus phải là bản ghi có ID lớn nhất VÀ Status phải bằng giá trị status truyền vào
                spec = spec.and((r, q, cb) -> cb.and(
                        cb.in(latestStatusJoin.get("id")).value(subquery), // latestStatusJoin.get("id") IN (Subquery)
                        cb.equal(latestStatusJoin.get("status"), status) // status = [status truyền vào]
                ));
            }

            // 5. Tìm kiếm chung (Query)
            if (keyword != null && !keyword.isEmpty()) {
                String likeQuery = "%" + keyword.toLowerCase() + "%";

                // JOIN tới Patient và User để tìm theo Tên và Mã bệnh nhân
                Join<Appointment, Patient> patientJoin = root.join("patient", JoinType.INNER);
                Join<Patient, User> userJoin = patientJoin.join("user", JoinType.INNER);
                Join<Doctor,Staff> staffJoin = doctorJoin.join("staff", JoinType.INNER);
                Join<Staff,User> doctorUserJoin = staffJoin.join("user", JoinType.INNER);

                Specification<Appointment> searchSpec = (rootSearch, querySearch, criteriaBuilderSearch) -> {
                    return criteriaBuilderSearch.or(
                            // Tìm theo Mã Bệnh nhân
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(patientJoin.get("patientCode")), likeQuery),
                            // Tìm theo Tên Bệnh nhân
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(userJoin.get("fullName")), likeQuery),
                            // Tìm theo SDT Bệnh nhân
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(userJoin.get("phoneNumber")), likeQuery),
                            // Tìm theo Mã lịch hẹn
                            criteriaBuilderSearch.like(rootSearch.get("code"), likeQuery),
                            // Tìm theo Tên Bác sĩ
                            criteriaBuilderSearch.like(criteriaBuilderSearch.lower(doctorUserJoin.get("fullName")), likeQuery)
                    );
                };
                spec = spec.and(searchSpec);
            }

            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }

    // Hàm hỗ trợ tạo Specification để lọc trường LocalDateTime trong phạm vi một ngày.
    private static Specification<Appointment> filterByDateRange(LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            // Tính thời điểm bắt đầu của ngày (ví dụ: 2025-11-01 00:00:00)
            LocalDateTime startOfDay = date.atStartOfDay();
            // Tính thời điểm kết thúc của ngày (ví dụ: 2025-11-01 23:59:59.999...)
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
            return criteriaBuilder.between(root.get("visitDateTime"), startOfDay, endOfDay);
        };
    }
}
