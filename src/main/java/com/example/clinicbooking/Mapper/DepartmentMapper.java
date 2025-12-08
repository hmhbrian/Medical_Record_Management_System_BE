package com.example.clinicbooking.Mapper;

import com.example.clinicbooking.DTO.Department.DepartmentResponse;
import com.example.clinicbooking.DTO.Department.DepartmentRpDetail;
import com.example.clinicbooking.DTO.Specialty.SpecialtyResponse;
import com.example.clinicbooking.entity.Department;
import com.example.clinicbooking.entity.Doctor;
import com.example.clinicbooking.entity.Specialty;
import com.example.clinicbooking.service.Department.DepartmentAuxService;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    // ===== Basic mappings (entity -> DTO skeleton) =====
    @Mapping(target = "head_doctor_name", expression = "java(headDoctorName(dept))")
    @Mapping(target = "number_of_staff", ignore = true)
    @Mapping(target = "number_of_specialties", ignore = true)
    @Mapping(target = "number_of_rooms", ignore = true)
    DepartmentResponse toResponse(Department dept, @Context DepartmentAuxService aux);

    @Mapping(target = "head_doctor_name", expression = "java(headDoctorName(dept))")
    @Mapping(target = "number_of_staff", ignore = true)
    @Mapping(target = "number_of_specialties", ignore = true)
    @Mapping(target = "number_of_rooms", ignore = true)
    @Mapping(target = "specialties", ignore = true)
    DepartmentRpDetail toDetail(Department dept, @Context DepartmentAuxService aux);

    // ===== Helper: map Specialty -> SpecialtyResponse =====
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "icon", source = "icon")
    SpecialtyResponse toSpecialtyResponse(Specialty s);

    // ===== AfterMapping để set counts & danh sách specialties =====
    @AfterMapping
    default void fillCounts(@MappingTarget DepartmentResponse dto,
            Department dept,
            @Context DepartmentAuxService aux) {
        int id = dept.getId();
        dto.setNumber_of_staff(aux.staffCount(id));
        dto.setNumber_of_specialties(aux.specialtyCount(id));
        dto.setNumber_of_rooms(aux.roomCount(id));
    }

    @AfterMapping
    default void fillCountsAndSpecialties(@MappingTarget DepartmentRpDetail dto,
            Department dept,
            @Context DepartmentAuxService aux) {
        int id = dept.getId();
        dto.setNumber_of_staff(aux.staffCount(id));
        dto.setNumber_of_specialties(aux.specialtyCount(id));
        dto.setNumber_of_rooms(aux.roomCount(id));

        List<SpecialtyResponse> sp = aux.specialtiesOf(id).stream()
                .map(this::toSpecialtyResponse)
                .toList();
        dto.setSpecialties(sp);
    }

    // Null-safe lấy tên trưởng khoa
    default String headDoctorName(Department dept) {
        if (dept == null)
            return null;
        Doctor d = dept.getHeadDoctor();
        if (d == null)
            return null;

        // Ưu tiên tên User nếu có
        try {
            if (d.getStaff() != null && d.getStaff().getUser() != null) {
                // đổi "getFullName()" thành "getName()" nếu User của bạn dùng name
                String name = d.getStaff().getUser().getFullname();
                if (name != null && !name.isBlank())
                    return name;
            }
        } catch (Exception ignored) {
        }

        // fallback: dùng doctorcode nếu không có tên user
        if (d.getDoctorcode() != null && !d.getDoctorcode().isBlank()) {
            return d.getDoctorcode();
        }
        // fallback cuối: "Bác sĩ #id"
        return "Doctor #" + d.getId();
    }
}
