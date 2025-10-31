package com.example.clinicbooking.service.Medicine;

import com.example.clinicbooking.DTO.Medicine.MedicineRequest;
import com.example.clinicbooking.DTO.Medicine.MedicineResponse;
import com.example.clinicbooking.DTO.Medicine.MedicineSummaryResponse;
import com.example.clinicbooking.entity.DrugType;
import com.example.clinicbooking.entity.Medicine;
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

    //map request data to entity
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

    //tạo mới thuốc
    public MedicineResponse create(MedicineRequest req) {
        Medicine m = new Medicine();
        applyRequestToEntity(req, m);
        repo.save(m);
        return MedicineResponse.fromEntity(m);
    }

    //lấy tất cả thuốc
    public List<MedicineResponse> getAll() {
        return repo.findAllByOrderByStatusDesc().stream().map(MedicineResponse::fromEntity).collect(Collectors.toList());
    }

    //lấy thuốc theo id
    public MedicineResponse getById(Integer id) {
        Medicine m = repo.findById(id).orElseThrow(() -> new InvalidInputException("Không tìm thấy thuốc"));
        return MedicineResponse.fromEntity(m);
    }

    //cập nhật thuốc
    public MedicineResponse update(Integer id, MedicineRequest req) {
        Medicine m = repo.findById(id).orElseThrow(() -> new InvalidInputException("Không tìm thấy thuốc"));
        applyRequestToEntity(req, m);
        repo.save(m);
        return MedicineResponse.fromEntity(m);
    }

    //xóa thuốc
    public void delete(Integer id) {
        Medicine m = repo.findById(id).orElseThrow(() -> new InvalidInputException("Không tìm thấy thuốc"));
        repo.delete(m);
    }

    //tìm kiếm thuốc theo tên
    public List<MedicineResponse> searchByName(String keyword) {
        List<Medicine> list = repo.findByMedicineNameContainingIgnoreCase(keyword);
        return list.stream().map(MedicineResponse::fromEntity).collect(Collectors.toList());
    }

    //lấy danh sách thuốc sắp hết hạn trong x ngày tới
    public List<MedicineResponse> getExpiringMedicines(int daysAhead) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, daysAhead);
        Date threshold = cal.getTime();
        List<Medicine> list = repo.findExpiringSoon(threshold);
        return list.stream().map(MedicineResponse::fromEntity).collect(Collectors.toList());
    }

    //tìm kiếm thuốc theo từ khóa trên nhiều trường(cho doctor tìm kiếm kê đơn)
    public List<MedicineSummaryResponse> getMedicineForPrescription(String keyword) {

        return repo.findAll(MedicineSpecification.searchByKeyword(keyword)).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private MedicineSummaryResponse convertToResponse(Medicine medicine) {
        MedicineSummaryResponse response = new MedicineSummaryResponse();
        response.setId(medicine.getId());
        response.setMedicineName(medicine.getMedicineName());
        response.setCurrent_quantity(medicine.getCurrent_quantity());
        response.setConcentration(medicine.getConcentration());
        response.setUnit(medicine.getUnit());
        response.setActive_ingredient(medicine.getActive_ingredient());
        return response;
    }

}
