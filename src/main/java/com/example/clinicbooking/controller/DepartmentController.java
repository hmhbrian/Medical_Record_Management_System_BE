package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.Department.DepartmentRequest;
import com.example.clinicbooking.DTO.Department.DepartmentResponse;
import com.example.clinicbooking.DTO.Department.DepartmentRpDetail;
import com.example.clinicbooking.entity.Department;
import com.example.clinicbooking.service.Department.DepartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Departments", description = "Quản lý các khoa")
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    private final DepartmentService service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    @GetMapping
    public List<DepartmentResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentRpDetail> getDepartmentById(@PathVariable int id) {
        return service.findDetailById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Department createDepartment(@RequestBody DepartmentRequest deptRq) {
        return service.save(deptRq);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateDepartment(@PathVariable int id,
            @RequestBody DepartmentRequest deptRq) {
        try {
            Department updated = service.update(id, deptRq);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật khoa thành công!", updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable int id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.ok("Cập nhật khoa thành công!");
    }
}
