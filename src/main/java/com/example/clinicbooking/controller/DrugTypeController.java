package com.example.clinicbooking.controller;

import com.example.clinicbooking.entity.DrugType;
import com.example.clinicbooking.entity.RoomTypes;
import com.example.clinicbooking.service.DrugTypeService;
import com.example.clinicbooking.service.RoomTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drugtypes")
@Tag(name = "DrugType", description = "Quản lý các loại thuốc")
public class DrugTypeController {

    private final DrugTypeService drugTypeService;

    public DrugTypeController(DrugTypeService drugTypeService) {
        this.drugTypeService = drugTypeService;
    }

    @GetMapping
    public List<DrugType> getAllRoomTypes() {
        return drugTypeService.findAll();
    }

    @PostMapping
    public DrugType createRoomType(@RequestBody DrugType drugType) {
        return drugTypeService.save(drugType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable int id) {
        if(!drugTypeService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        drugTypeService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
