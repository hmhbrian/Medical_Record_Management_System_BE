package com.example.clinicbooking.service.Department;

import com.example.clinicbooking.DTO.Department.DepartmentRequest;
import com.example.clinicbooking.DTO.Department.DepartmentResponse;
import com.example.clinicbooking.DTO.Department.DepartmentRpDetail;
import com.example.clinicbooking.Mapper.DepartmentMapper;
import com.example.clinicbooking.entity.Department;
import com.example.clinicbooking.entity.Doctor;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.DepartmentRepository;
import com.example.clinicbooking.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository repo;
    private final DoctorRepository doctorRepo;
    private final DepartmentMapper departmentMapper;
    private final DepartmentAuxService aux;


    public List<DepartmentResponse> findAll() {
        return repo.findAll()
                .stream()
                .map(d -> departmentMapper.toResponse(d, aux))
                .toList();
    }

    public Optional<DepartmentRpDetail> findDetailById(int id) {
        return repo.findById(id)
                .map(d -> departmentMapper.toDetail(d, aux));
    }

    public Optional<Department> findById(int id) { return repo.findById(id); }

    public Department save(DepartmentRequest deptRq) {
        Department dept = new Department();
        dept.setName(deptRq.getName());
        dept.setDescription(deptRq.getDescription());
        dept.setContact(deptRq.getContact());
        dept.setEstablishment_date(deptRq.getEstablishment_date());
        dept.setStatus(deptRq.getStatus());
        // Chỉ tìm và gán headDoctor nếu có id hợp lệ
        if (deptRq.getHead_doctor_id() != 0) {
            Doctor headDoctor = doctorRepo.findById(deptRq.getHead_doctor_id())
                    .orElseThrow(() -> new InvalidInputException(
                            "Head doctor not found with id: " + deptRq.getHead_doctor_id()));
            dept.setHeadDoctor(headDoctor);
        } else {
            dept.setHeadDoctor(null);
        }
        return repo.save(dept);
    }

    public Department update(int id, DepartmentRequest deptRq) {
        Department dept = repo.findById(id)
                .orElseThrow(() -> new InvalidInputException("Department not found with id: " + id));

        // Nếu request có headDoctorId thì lấy ra Doctor
        if (deptRq.getHead_doctor_id() > 0) {
            Doctor headDoctor = doctorRepo.findById(deptRq.getHead_doctor_id())
                    .orElseThrow(() -> new InvalidInputException("Head doctor not found with id: " + deptRq.getHead_doctor_id()));
            dept.setHeadDoctor(headDoctor);
        }

        // Cập nhật các field khác
        dept.setName(deptRq.getName());
        dept.setDescription(deptRq.getDescription());
        dept.setContact(deptRq.getContact());
        dept.setEstablishment_date(deptRq.getEstablishment_date());
        dept.setStatus(deptRq.getStatus());

        return repo.save(dept);
    }

    public void delete(int id) {
        Department dept = repo.findById(id)
                .orElseThrow(() -> new InvalidInputException("Department not found with id: " + id));
        repo.deleteById(id);
    }
}
