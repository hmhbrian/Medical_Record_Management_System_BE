package com.example.ClinicBooking.service;

import com.example.ClinicBooking.DTO.MedicineRequest;
import com.example.ClinicBooking.DTO.MedicineResponse;
import com.example.ClinicBooking.entity.Medicine;
import com.example.ClinicBooking.repository.MedicineRepository;
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

    public MedicineResponse create(MedicineRequest req) {
        Medicine m = new Medicine();
        m.setMedicineName(req.medicineName);
        m.setUnit(req.unit);
        m.setStockQuantity(req.stockQuantity);
        m.setExpirationDate(req.expirationDate);
        m.setPrice(req.price);
        repo.save(m);
        return MedicineResponse.fromEntity(m);
    }

    public List<MedicineResponse> getAll() {
        return repo.findAll().stream().map(MedicineResponse::fromEntity).collect(Collectors.toList());
    }

    public MedicineResponse getById(Integer id) {
        Medicine m = repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy thuốc"));
        return MedicineResponse.fromEntity(m);
    }

    public MedicineResponse update(Integer id, MedicineRequest req) {
        Medicine m = repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy thuốc"));
        m.setMedicineName(req.medicineName);
        m.setUnit(req.unit);
        m.setStockQuantity(req.stockQuantity);
        m.setExpirationDate(req.expirationDate);
        m.setPrice(req.price);
        repo.save(m);
        return MedicineResponse.fromEntity(m);
    }

    public void delete(Integer id) {
        Medicine m = repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy thuốc"));
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
