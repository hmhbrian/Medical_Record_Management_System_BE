package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.Medicine.MedicineRequest;
import com.example.clinicbooking.DTO.Medicine.MedicineResponse;
import com.example.clinicbooking.entity.DrugType;
import com.example.clinicbooking.entity.Medicine;
import com.example.clinicbooking.entity.Specialty;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.DrugTypeRepository;
import com.example.clinicbooking.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicineService {
    @Autowired
    private MedicineRepository repo;
    @Autowired
    private DrugTypeRepository drugTypeRepo;

    private void applyRequestToEntity(MedicineRequest req, Medicine m) {
        DrugType drugType = drugTypeRepo.findById(req.getDrugtype_id())
                .orElseThrow(() -> new InvalidInputException("DrugType not found with id: " + req.getDrugtype_id()));

        m.setMedicineName(req.getMedicineName());
        m.setUnit(req.getUnit());
        m.setExpirationDate(req.getExpirationDate());
        m.setProductionDate(req.getProductionDate());
        m.setPrice(req.getPrice());
        m.setConcentration(req.getConcentration());
        m.setManufacturer(req.getManufacturer());
        m.setActive_ingredient(req.getActive_ingredient());
        m.setDosage_form(req.getDosage_form());
        m.setCurrent_quantity(req.getCurrent_quantity());
        m.setMinimum_quantity(req.getMinimum_quantity());
        m.setStatus(req.getStatus());
        m.setDrugType(drugType);
    }

    public MedicineResponse create(MedicineRequest req) {
        Medicine m = new Medicine();
        applyRequestToEntity(req, m);
        repo.save(m);
        return MedicineResponse.fromEntity(m);
    }

    public List<MedicineResponse> getAll() {
        return repo.findAllByOrderByStatusDesc().stream().map(MedicineResponse::fromEntity).collect(Collectors.toList());
    }

    public MedicineResponse getById(Integer id) {
        Medicine m = repo.findById(id).orElseThrow(() -> new InvalidInputException("Không tìm thấy thuốc"));
        return MedicineResponse.fromEntity(m);
    }

    public MedicineResponse update(Integer id, MedicineRequest req) {
        Medicine m = repo.findById(id).orElseThrow(() -> new InvalidInputException("Không tìm thấy thuốc"));
        applyRequestToEntity(req, m);
        repo.save(m);
        return MedicineResponse.fromEntity(m);
    }

    public void delete(Integer id) {
        Medicine m = repo.findById(id).orElseThrow(() -> new InvalidInputException("Không tìm thấy thuốc"));
        repo.delete(m);
    }

    public List<MedicineResponse> searchByName(String keyword) {
        List<Medicine> list = repo.findByMedicineNameContainingIgnoreCase(keyword);
        return list.stream().map(MedicineResponse::fromEntity).collect(Collectors.toList());
    }

    public List<MedicineResponse> getExpiringMedicines(int daysAhead) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, daysAhead);
        Date threshold = cal.getTime();
        List<Medicine> list = repo.findExpiringSoon(threshold);
        return list.stream().map(MedicineResponse::fromEntity).collect(Collectors.toList());
    }

}
