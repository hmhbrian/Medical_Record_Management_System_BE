package com.example.clinicbooking.controller;

import com.example.clinicbooking.entity.RoomTypes;
import com.example.clinicbooking.service.RoomTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roomtypes")
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @GetMapping
    public List<RoomTypes> getAllRoomTypes() {
        return roomTypeService.findAll();
    }

    @PostMapping
    public RoomTypes createRoomType(@RequestBody RoomTypes roomType) {
        return roomTypeService.save(roomType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable int id) {
        if(!roomTypeService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        roomTypeService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
