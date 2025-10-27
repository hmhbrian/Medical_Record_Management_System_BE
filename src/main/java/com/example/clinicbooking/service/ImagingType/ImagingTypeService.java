package com.example.clinicbooking.service.ImagingType;

import com.example.clinicbooking.DTO.Services.ImagingTypeResponse;
import com.example.clinicbooking.DTO.Services.TestTypeResponse;
import com.example.clinicbooking.entity.ImagingTypes;
import com.example.clinicbooking.entity.TestTypes;
import com.example.clinicbooking.repository.ImagingTypeRepository;
import com.example.clinicbooking.repository.TestTypeRepository;
import com.example.clinicbooking.service.TestType.TestTypeSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImagingTypeService {
    @Autowired
    public ImagingTypeRepository imagingTypeRepo;

    public List<ImagingTypeResponse> search(String keyword) {
        return imagingTypeRepo.findAll(ImagingTypeSpecification.searchByKeyword(keyword))
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    private ImagingTypeResponse covertToResponse(ImagingTypes imagingTypes) {
        ImagingTypeResponse dto = new ImagingTypeResponse();
        dto.setId(imagingTypes.getId());
        dto.setImagingCode(imagingTypes.getImagingCode());
        dto.setImagingName(imagingTypes.getImagingName());
        dto.setPrice(imagingTypes.getPrice());
        dto.setDescription(imagingTypes.getDescription());
        return dto;
    }
}
