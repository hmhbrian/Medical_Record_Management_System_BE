package com.example.clinicbooking.service.Appointment;

import com.example.clinicbooking.entity.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentSpecification {
    public static Specification<Appointment> buildSearchAppointmentSpec(
            String keyword,
            int status,
            Integer departmentId,
            LocalDate fromDate
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Join các cấu trúc liên quan
            // Appointment -> Patient -> User
            Join<Appointment, Patient> patientJoin = root.join("patient");
            Join<Patient, User> patientUserJoin = patientJoin.join("user");

            // Appointment -> Doctor -> Staff -> User
            Join<Appointment, Doctor> doctorJoin = root.join("doctor");
            Join<Doctor, Staff> staffJoin = doctorJoin.join("staff");
            Join<Staff, User> doctorUserJoin = staffJoin.join("user");

            // Appointment -> DoctorSchedule
            Join<Appointment, DoctorSchedules> scheduleJoin = root.join("doctorSchedule");

            // Appointment -> Doctor -> Specialty -> Department
            Join<Doctor, Specialty> specialtyJoin = doctorJoin.join("specialty");
            Join<Specialty, Department> deptJoin = specialtyJoin.join("department");

            // 1) Keyword: bệnh nhân (fullname), mã lịch hẹn (code), bác sĩ (fullname)
            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword.trim().toLowerCase() + "%";
                Predicate byPatientName = cb.like(cb.lower(patientUserJoin.get("fullname")), like);
                Predicate byCode        = cb.like(cb.lower(root.get("code")), like);
                Predicate byDoctorName  = cb.like(cb.lower(doctorUserJoin.get("fullname")), like);
                predicates.add(cb.or(byPatientName, byCode, byDoctorName));
            }

            // 2) Khoa (department)
            if (departmentId != null) {
                predicates.add(cb.equal(deptJoin.get("id"), departmentId));
            }

            // 3) fromDate: date của doctorSchedule >= fromDate
            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(scheduleJoin.get("date"), fromDate));
            }

            // 4) Status của bản ghi AppointmentStatus MỚI NHẤT
            if (status > 0) {
                // Subquery: lấy MAX(updateAt) theo appointment.id
                Subquery<LocalDateTime> subMaxUpdate = query.subquery(LocalDateTime.class);
                Root<AppointmentStatus> s1 = subMaxUpdate.from(AppointmentStatus.class);
                // Ép kiểu generic tại đây:
                Path<LocalDateTime> updateAtPath = s1.<LocalDateTime>get("updateAt");

                subMaxUpdate.select(cb.greatest(updateAtPath))
                        .where(cb.equal(s1.get("appointment").get("id"), root.get("id")));

                // Alias join AppointmentStatus để so sánh với subquery
                Subquery<Integer> existsLatest = query.subquery(Integer.class);
                Root<AppointmentStatus> s2 = existsLatest.from(AppointmentStatus.class);

                existsLatest.select(cb.literal(1))
                        .where(
                                cb.equal(s2.get("appointment").get("id"), root.get("id")),
                                cb.equal(s2.get("status"), status),                 // status là int
                                cb.equal(s2.get("updateAt"), subMaxUpdate)          // so khớp với MAX(updateAt)
                        );

                // Quan trọng: tránh nhân bản bản ghi do join thêm bảng s2
                predicates.add(cb.exists(existsLatest));
                query.distinct(true);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


}
