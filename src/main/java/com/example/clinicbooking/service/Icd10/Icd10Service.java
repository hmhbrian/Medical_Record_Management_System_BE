package com.example.clinicbooking.service.Icd10;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.Icd10.Icd10Reponse;
import com.example.clinicbooking.DTO.Icd10.Icd10Request;
import com.example.clinicbooking.entity.Icd10;
import com.example.clinicbooking.repository.Icd10Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class Icd10Service {
    @Autowired
    private Icd10Repository icd10Repo;

    public List<Icd10Reponse> search(String keyword) {
        return icd10Repo.findAll(Icd10Specification.searchByKeyword(keyword))
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    public ApiResponse<Icd10Reponse> create(Icd10Request request) {
        if (request.getCode() == null || icd10Repo.existsByCode(request.getCode())) {
            return new ApiResponse<>(false, "ICD-10 code trống hoặc đã tồn tại: " + request.getCode(), null);
        }
        if (request.getNameVn() == null) {
            return new ApiResponse<>(false, "ICD-10 nameVN không được để trống!", null);
        }
        if (request.getNameEn() == null) {
            return new ApiResponse<>(false, "ICD-10 nameEn không được để trống !", null);
        }
        Icd10 icd10 = new Icd10();
        icd10.setCode(request.getCode());
        icd10.setNameVn(request.getNameVn());
        icd10.setNameEn(request.getNameEn());
        icd10.setCategory(request.getCategory());
        icd10Repo.save(icd10);

        return new ApiResponse<>(true, "Thêm mã bệnh thành công", covertToResponse(icd10));
    }

    public ApiResponse<Icd10Reponse> update(Integer id, Icd10Request request) {
        Optional<Icd10> optionalIcd10 = icd10Repo.findById(id);
        if (optionalIcd10.isEmpty()) {
            return new ApiResponse<>(false, "Không tìm thấy mã bệnh ICD-10 với ID: " + id, null);
        }

        Icd10 icd10 = optionalIcd10.get();

        //Kiểm tra và cập nhật Code (nếu có và không bị trùng với mã khác)
        if (request.getCode() != null) {
            // Kiểm tra xem mã mới có trùng với mã ICD-10 khác (ngoài chính nó) không
            if (!request.getCode().equals(icd10.getCode()) && icd10Repo.existsByCode(request.getCode())) {
                return new ApiResponse<>(false, "Mã bệnh " + request.getCode() + " đã tồn tại.", null);
            }
            icd10.setCode(request.getCode());
        }

        if (request.getNameVn() != null) {
            icd10.setNameVn(request.getNameVn());
        }
        if (request.getNameEn() != null) {
            icd10.setNameEn(request.getNameEn());
        }
        if (request.getCategory() != null) {
            icd10.setCategory(request.getCategory());
        }

        Icd10 updatedIcd10 = icd10Repo.save(icd10);
        return new ApiResponse<>(true, "Cập nhật mã bệnh ICD-10 thành công", covertToResponse(updatedIcd10));
    }

    public ApiResponse<Void> delete(Integer id) {
        if (!icd10Repo.existsById(id)) {
            return new ApiResponse<>(false, "Không tìm thấy mã bệnh ICD-10 với ID: " + id, null);
        }

        icd10Repo.deleteById(id);

        return new ApiResponse<>(true, "Xóa mã bệnh ICD-10 thành công", null);
    }

    private Icd10Reponse covertToResponse(Icd10 icd10) {
        Icd10Reponse dto = new Icd10Reponse();
        dto.setId(icd10.getId());
        dto.setCode(icd10.getCode());
        dto.setNameVn(icd10.getNameVn());
        dto.setNameEn(icd10.getNameEn());
        return dto;
    }
}

